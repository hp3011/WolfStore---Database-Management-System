package main.java.app;
import java.sql.*;  
import java.util.Scanner;
import org.apache.ibatis.jdbc.ScriptRunner;


public class app {
    static Connection conn;
    static String userid;
    static String password;

    
    public static void main(String[] args) {
        
        // Setup db connection w username and password
        conn = getConnection();

        // Create tables- loop through CREATE TABLE statements and execute each



    }

    public static Connection getConnection(){

        Scanner in = new Scanner(System.in);
        System.out.print("Enter database user id: ");
        userid = in.nextLine();

        System.out.print("Enter database password: ");
        password = in.nextLine();
        in.close();

        Class.forName("org.mariadb.jdbc.Driver");

        try {
            return DriverManager.getConnection("jdbc:mariadb://classdb2.csc.ncsu.edu:3306/" + userid, userid, password);
        } catch(Exception e){
            System.out.println(e);
        }
    }

}
