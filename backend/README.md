# The Backend to FlickPick Application

## Backend
Change into the backend directory:
```cd flick_pick```

If running for the first time, create virtual environment, activate it, and install dependencies:
```python -m venv venv```
```venv\Scripts\activate```
```pip install -r requirements.txt```

If you encounter a security error in Powershell, run before activating the environment:
```Set-ExecutionPolicy Unrestricted -Scope Process```

Start the app with uvicorn:
```uvicorn main:app --reload --port 9000```

Visit ```http://127.0.0.1:9000/docs``` to get started with APIs.

## Database
To set up the database for the first time, ensure MySQL server (with default port 3306) is installed and started locally.

Change into the database directory:
```cd database```

Run the setup file to initialize DB and Tables:
```mysql -u root -p < setup.sql```
or in Powershell:
```Get-Content .\setup.sql | mysql -u root -p```

(Optional) Similarly, populate tables with mock users and groups:
```mysql -u root -p < seed.sql```
or in Powershell:
```Get-Content .\seed.sql | mysql -u root -p```

Check if db and tables exists:
```mysql -u root -p```
```
SHOW DATABASES;
USE flick_pick;
SHOW TABLES;
```

Populate Database by running populate_db.py script (make sure in virtual environment and dependencies installed):
```python populate_db.py```

To Update the DB, drop the database and rerun the setup.sql script.
