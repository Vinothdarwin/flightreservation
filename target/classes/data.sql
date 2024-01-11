REPLACE INTO `roles` VALUES (1,'ADMIN');
REPLACE INTO `roles` VALUES (2,'USER');
REPLACE INTO `customers` (user_id,active,email,last_name,name,password,user_name) VALUES (0,1,'admin@gmail.com','ADMIN','ADMIN','$2a$10$0UQxZDhAYFBDhYJMIrKNweTpiB5fBXsOzqKuEDmYrJOCNum1kTHxS','admin');
REPLACE INTO `user_role` VALUES (0, 1);
--Insert into flights (flight_id,date,destination,fare,name,source,time,seats) Values (1,'02/02/2000','chennai',200,'indian express','dubai',200,20);