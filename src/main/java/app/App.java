package main.java.app;
import java.sql.*;  
import java.util.Scanner;
import java.util.regex.Pattern;

// when in remote server run from CSC540_WolfWR/src
// javac main/java/app/*.java then java main/java/app/App

public class App {
    static Connection conn;
    static String userid;
    static String password;
    static int menu = 1;
    static int input;
    static boolean exit = false;
    static Pattern phonePattern = Pattern.compile("\\d{1}-\\d{3}-\\d{3}-\\d{4}");
    private static Scanner in;

    public static void main(String[] args) {
        
        // Setup db connection w username and password
        System.out.println("Connecting to database...");
        conn = getConnection();
        // TO DO if connection can't be made then try again or exit

        // Create db and tables- loop through CREATE TABLE statements and execute each
        System.out.println("Loading data...");
        setupDb();

        in = new Scanner(System.in);

        // show main menu
        showOptions(1);

        while (exit == false) {
            
            try {input = in.nextInt();} catch (Exception e) {System.out.println(e);}


            switch (menu) {

                // quit
                case 0:
                    exit = true;
                    break;

                // main menu
                case 1:
                    // Check for valid menu option
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
                // Owner: Jake
                case 2:
                    switch (input) {
                        // Exit
                        case 0:
                            exit = true;
                            System.out.println("Exiting...");
                            break;
                        // Return to main menu
                        case 1:
                            menu = 1;
                            System.out.println("Returning to main menu");
                            showOptions(1);
                            break;
                        case 2:
                            signUpMember(conn);
                            break;
                        // To do: Build out remaining options

                    }

                // billing staff menu
                // Owner: Mithil
                case 3:
                    switch (input) {
                        // Exit
                        case 0:
                            exit = true;
                            System.out.println("Exiting...");
                            break;

                        // Return to main menu
                        case 1:
                            menu = 1;
                            System.out.println("Returning to main menu");
                            showOptions(1);
                            break;

                        // To do: Build out remaining options
                        // case 2:
                    }

                // warehouse operator menu
                // Owner: Hrishikesh
                case 4:
                    switch (input) {
                        // Exit
                        case 0:
                            exit = true;
                            System.out.println("Exiting...");
                            break;

                        // Return to main menu
                        case 1:
                            menu = 1;
                            System.out.println("Returning to main menu");
                            showOptions(1);
                            break;

                        // To do: Build out remaining options
                        //case 2:
                    }

                // admin menu
                // Owner: Parimal
                case 5:
                    switch (input) {

                        // Exit
                        case 0:
                            exit = true;
                            System.out.println("Exiting...");
                            break;

                        // Return to main menu
                        case 1:
                            menu = 1;
                            System.out.println("Returning to main menu");
                            showOptions(1);
                            break;

                        // To do: Build out remaining options
                        //case 2:
                    }

                default:
                    break;
            }
        }
        in.close();
    }

    public static Connection getConnection(){

        Connection c = null;

        in = new Scanner(System.in);
        System.out.print("Enter database user id: ");
        userid = in.nextLine();

        System.out.print("Enter database password: ");
        password = in.nextLine();

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
            System.out.println("Which department do you belong to? Enter the corresponding number:");
            System.out.println("2 - Registration Staff\n3 - Billing Staff\n4 - Warehouse\n5 - Admin\nEnter 0 to quit");
            break;

            // registration staff options
            case 2:
            System.out.println("Welcome registration staff. Please choose from the available options below:");
            System.out.println("\t0 - Exit program\n\t1 - Return to main menu");
            System.out.println("\t2 - Signup a new club member");
            break;

            // billing staff options
            case 3:
            System.out.println("Welcome billing staff. Please choose from the available options below:");
            System.out.println("\t0 - Exit program\n\t1 - Return to main menu");           
            break;

            // warehouse operator options
            case 4:
            System.out.println("Welcome warehouse staff. Please choose from the available options below:");
            System.out.println("\t0 - Exit program\n\t1 - Return to main menu");
            break;

            // admin options
            case 5:
            System.out.println("Welcome admin. Please choose from the available options below:");
            System.out.println("\t0 - Exit program\n\t1 - Return to main menu");
            break;
        }
    }

    public static void signUpMember(Connection con){
        // INSERT INTO ClubMember (CustomerID,ActiveStatus,Name,MembershipLevel,Address,Phone,Email)
        String name;
        String address;
        String phone;
        String email;
        String query;
        int customerId=0;
        

        Scanner in = new Scanner(System.in);

        System.out.println("Beginning new member signup. Enter the following user information:");

        System.out.println("Name:");
        name = in.nextLine();

        System.out.println("Address:");
        address = in.nextLine();

        // Get phone number, validate format
        System.out.println("Phone number (format #-###-###-####):");
        while (!in.hasNext(phonePattern)){
            System.out.println("Invalid phone number format, try again");
            in.nextLine();
        };
        phone = in.nextLine();

        System.out.println("Email address:");
        email = in.nextLine();

        // Get the next available CustomerID
        try (Statement stmt = con.createStatement()){
            ResultSet rs = stmt.executeQuery("SELECT max(CustomerID) as maxid FROM ClubMember");
            while (rs.next()) {
                int maxid = rs.getInt("maxid");
                customerId = maxid+1;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }


        query = String.format("INSERT INTO ClubMember VALUES (%s, \"Active\", \"%s\", \"Standard\", \"%s\", \"%s\", \"%s\")", customerId, name, address, phone, email);

        try (Statement stmt = con.createStatement()){
            stmt.executeQuery(query);
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println(query);
        }
    }
}