package main.java.app;
import java.sql.*;  
import java.util.Scanner;

// when in remote server run from CSC540_WolfWR/src

public class App {
    static Connection conn;
    static String userid;
    static String password;

    
    public static void main(String[] args) {
        
        // Setup db connection w username and password
        conn = getConnection();

        // Create db and tables- loop through CREATE TABLE statements and execute each
        createTables();


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

    public static void createTables() {
        Statement stmt = null;

        try {
            
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
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

