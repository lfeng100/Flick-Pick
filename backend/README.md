# The Backend to FlickPick Application

## Database
To set up the database for the first time, ensure MySQL server is installed and started.

Change into the database directory:
```cd database```

Run the setup file to initialize DB and Tables:
```mysql -u root -p < setup.sql```
or in Powershell:
```Get-Content .\setup.sql | mysql -u root -p```

Check if table exists:
```mysql -u root -p```
```
SHOW DATABASES;
USE movie_app;
SHOW TABLES;
```
