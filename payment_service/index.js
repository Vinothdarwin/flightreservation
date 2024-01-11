var apminsight = require('apminsight')();
const e = require("express");
var express = require("express");
var mysql = require('mysql');
const redis = require("redis");
const redisClient = redis.createClient();
var connection = mysql.createConnection({
    host: process.env.MYSQL_HOST ? process.env.MYSQL_HOST : 'localhost',
    user: process.env.MYSQL_USER ? process.env.MYSQL_USER :  'root',
    password: process.env.MYSQL_PASSWORD ? process.env.MYSQL_PASSWORD :  '',
    database: process.env.MYSQL_DATABASE ? process.env.MYSQL_DATABASE :  'test'
});


var app = express(); app.listen(3000, () => {
    console.log("Server running on port 3000");
    connection.connect();
    connection.query('CREATE TABLE IF NOT EXISTS payments (payment_id VARCHAR(40), booking_id VARCHAR(20), type VARCHAR(20))', function (error, results, fields) {
        if (error) {
            console.error(error);
        }
    });
    setInterval(function(){
      apminsight.startBackgroundTransaction('cronjob', function(){
        setTimeout(function(){
            redisGetterSetter();
            connection.query('SELECT SLEEP(2)', function (error, results, fields) {});
            apminsight.endTransaction();
        },1000);
      });
    }, 500000);
});

app.use(express.json())

var redisGetterSetter=()=>{
    const redisClient = redis.createClient();
    redisClient.on("error", function (err) {
      console.log("Error " + err);
    });
    redisClient.set("string key", "string val", redis.print);
    redisClient.hset("hash key", "hashtest 1", "some value", redis.print);
    redisClient.hset(["hash key", "hashtest 2", "some other value"], redis.print);
    redisClient.hkeys("hash key", function (err, replies) {
      console.log(replies.length + " replies:");
      replies.forEach(function (reply, i) {
          console.log("    " + i + ": " + reply);
      });
      redisClient.quit();
    });
}

app.get("/statuscheck",function(req,res){
    res.send({"message":"success"});
})

app.post("/payment", (req, res, next) => {
    console.log(req.body);
    apminsight.addParameter("booking_id", req.body.booking_id);
    connection.query('SELECT SLEEP(2)', function (error, results, fields) {
        if (req.body.booking_id && req.body.type) {
            connection.query("INSERT INTO payments values(uuid(),?,?)", [req.body.booking_id, req.body.type], function (error, results, fields) {
                if (error) {
                    console.error(error);
                }else{
                    connection.query("SELECT * from payments where booking_id=?", [req.body.booking_id], function(error, payments, fields){
                        try{
                            redisGetterSetter();
                            require('vm').runInThisContext('binary ! isNotOk');
                        }catch(err){
                            apminsight.trackError(err);
                        }
                        res.json(payments)
                    })
                }
            });
        } else {
            res.status(400).send('Invalid input parameters');
        }
    });
});