# CSC540_WolfWR

FILE DESCRIPTIONS:
1. src/main/java/App.java: main file that runs the application
2. src/main/java/ReadFileToStringArray.java: just used for a helper function, can ignore for your purposes
3. src/main/sql/create_tables.txt: a list of all the SQL commands to execute to setup the database. App.java calls ReadFileToStringArray.java which reads all the commands in this file

HOW TO RUN:
1. From within a terminal that is connected to NCSU server, clone repo
2. cd CSC540_WolfWR
3. javac main/java/app/*.java 
4. java main/java/app/App

CURRENT STATUS:
1. Prompts user for db login
2. Connects to db
3. Clears existing tables
4. Creates tables
5. Inserts initial data
