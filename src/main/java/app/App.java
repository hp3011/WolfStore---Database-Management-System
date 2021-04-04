package main.java.app;
import java.sql.*;  
import java.util.Scanner;

// when in remote server run from CSC540_WolfWR/src
// javac main/java/app/*.java then java main/java/app/App

public class App {
    static Connection conn;
    static String userid;
    static String password;
    static int menu = 0;
    static int input;
    static boolean exit = false;
    
    public static void main(String[] args) {
        
        // Setup db connection w username and password
        System.out.println("Connecting to database...");
        conn = getConnection();

        // Create db and tables- loop through CREATE TABLE statements and execute each
        System.out.println("Loading data...");
        setupDb();

        Scanner in = new Scanner(System.in);

        // show main menu
        showOptions(0);

        while (exit == false) {
            
            input = in.nextInt();

            switch (menu) {

                // main menu
                case 1:
                    if (input > 0 && input <5){
                        // Display menu options
                        showOptions(input);
                        // Set menu to user's selected option
                        menu = input;
                    }
                    else if (input == 0){
                        exit = true;
                        break;
                    }
                    else {
                        System.out.println("Not a valid option, try again");
                    }
                    break;

                // registration staff menu
                case 2:
                // billing staff menu
                case 3:
                // warehouse operator menu
                case 4:
                // admin menu
                case 5:

                default:
                    break;
            }
        }

    }

    public static Connection getConnection(){

        Connection c = null;

        Scanner in = new Scanner(System.in);
        System.out.print("Enter database user id: ");
        userid = in.nextLine();

        System.out.print("Enter database password: ");
        password = in.nextLine();
        in.close();

        try {
            Class.forName("org.mariadb.jdbc.Driver");
         } catch (ClassNotFoundException e) {
            System.out.println(e);
        }

        try {
            c = DriverManager.getConnection("jdbc:mariadb://classdb2.csc.ncsu.edu:3306/" + userid, userid, password);
        } catch(Exception e){
            System.out.println(e);
        }
        return c;
    }

    public static void setupDb() {
        Statement stmt = null;

        try {
            
            // reads in sql command file, splits file to array by new lines resulting in an array of sql commands
            String[] arrCommands = ReadFileToStringArray.readLineByLine();

            // Execute each SQL command
            try {
                stmt = conn.createStatement();
            } catch (Exception e) { System.out.println(e); }

            for (String a : arrCommands){
                try {
                    //System.out.println(a);
                    stmt.executeUpdate(a);
                } catch (Exception e) {
                    System.out.println(e);
                    System.out.println(a);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showOptions(int view){
        switch(view) {
            // main menu options
            case 1:
            // registration staff options
            case 2:
            // billing staff options
            case 3:
            // warehouse operator options
            case 4:
            // admin options
            case 5:
        }
    }
}

