-- create databases
CREATE DATABASE IF NOT EXISTS `spring_microservices_customer`;
CREATE DATABASE IF NOT EXISTS `spring_microservices_fraud`;

-- create root user and grant rights
CREATE USER 'root'@'localhost' IDENTIFIED BY 'local';
GRANT ALL ON `spring_microservices_customer`.* TO 'root'@'%';
GRANT ALL ON `spring_microservices_fraud`.* TO 'root'@'%';