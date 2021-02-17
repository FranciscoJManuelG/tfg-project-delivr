# TFG Project 

## Requirements

- Node 12.14.0+.
- Yarn 1.21.1+.
- Java SE 8+.
- Maven 3+.
- MySQL 8+.

## Database creation

```
Start Mysql server if not running (e.g. mysqld).

mysqladmin -u root create tfgproject
mysqladmin -u root create tfgprojecttest

mysql -u root
    CREATE USER 'tfg'@'localhost' IDENTIFIED BY 'tfg';
    GRANT ALL PRIVILEGES ON tfgproject.* to 'tfg'@'localhost' WITH GRANT OPTION;
    GRANT ALL PRIVILEGES ON tfgprojecttest.* to 'tfg'@'localhost' WITH GRANT OPTION;
    exit
```

## Run

```
Execute the mysqld command first, to start the database

cd backend
mvn sql:execute (only first time to create tables)
mvn spring-boot:run

cd frontend
yarn install (only first time to download libraries)
yarn start
```
