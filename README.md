# flightreservation

## Description
    This application consists of both admin and user portal.It tracks the information about booking of flight tickets , number of upcoming flights.It mainly focuses on ticket booking function.
  
## Dependencies

  ###### Maven
    Maven helps to download dependencies required for the project.There is no need for dependency injection and can easily build project to jar ,war file.

  ######    Mysql
    It is an open-source relational database management system. It stores data in tables made up of rows and columns. Users can define, manipulate, and query data using SQL-Structured Query Language.
  
  ######    Redis
    It is an in-memory data structure store used as database, cache. It provides data structures such as string,hashes,lists,sets. It achieve top performance by working with in-memory dataset.it persist data by dumping the dataset to disk.
  
  ######    NodeJS
    It is an open source and cross platform JavaScript runtime environment.As all API in node.js are asynchronous, there is no need to wait for API to return.
  
  
## Setup
  ###### Running an application without docker
    1. Clone the application 
    2. Mysql changes 
          Create a new user (optional)
          Update user and password in application.properties file
    3. Validate mysql host, redis host, payment host in application.properties file
    4. Build the application with Maven 
    5. Run with java -jar target.jar
    
###### Running an application with Docker (optional)
    1. Build an image from a dockerfile using docker build 
    2. Run a docker image using docker run, with network="host" command
    
##  Troubleshooting

 ###### SqlExceptionHelper - Communications link failure
        1. Check the host mentioned in the application.properties file and change it according to your need.
 ###### Access denied for user 'springuser'@'localhost'
        1. Check the username and password for database in application.properties file
        2. Update the column of host as "%" for the username which was mentioned in application.properties file. (incase of docker)    
        
##  Concepts covered
   - [x] Spring boot
   - [x] Thymeleaf
   - [x] Hibernate
   - [x] Dependency Injection
   - [x] Cross platform communication
   - [ ] Unit testing - Junit
   - [ ] APMInsight intrumentation
  
