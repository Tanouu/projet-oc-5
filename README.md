# Procedure to install the project

## Procedure to install the database

### Install MySQL

Download and install MySQL from [the official site](https://dev.mysql.com/downloads/).

### Create the database

Connect to MySQL and execute the following command to create the database:

```sql
CREATE DATABASE your_database_name;
```

### Import the SQL script

Import the SQL script to create the tables and insert initial data:

```ini
ressources/sql/script.sql
```

### Install dependencies

```bash
cd front
npm install
```

### Launch the front-end

```bash
cd front
ng serve
```

### Launch the back-end

```ini
run back/src/test/java/com/openclassrooms/starterjwt/SpringBootSecurityJwtApplicationTests.java
```
