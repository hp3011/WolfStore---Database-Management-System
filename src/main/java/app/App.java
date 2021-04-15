package main.java.app;
import java.math.BigDecimal;
import java.sql.*;  
import java.util.*;
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

    private static PreparedStatement prepAddDiscount;
    private static PreparedStatement prepUpdateDiscount;
    private static PreparedStatement prepDeleteDiscount;

    private static PreparedStatement prepUpdateCustomer;
    private static PreparedStatement prepGetCustomer;

    private static PreparedStatement prepAddMerchandise;
    private static PreparedStatement prepUpdateMerchandise;
    private static PreparedStatement prepDeleteMerchandise;

    private static PreparedStatement prepAddTransaction;
    private static PreparedStatement prepUpdateTransaction;
    private static PreparedStatement prepDeleteTransaction;

    private static PreparedStatement prepAddStaff;
    private static PreparedStatement prepUpdateStaff;
    private static PreparedStatement prepDeleteStaff;
    private static PreparedStatement prepGetManager;

    private static PreparedStatement prepAddStore;
    private static PreparedStatement prepGetStores;
    private static PreparedStatement prepGetStore;
    private static PreparedStatement prepDeleteStore;
    private static PreparedStatement prepUpdateStore;

    //Add SQL query Statement here.
    public static void generatePreparedStatement(){
        try {
            String sql;

            //Rewards Table
            sql = "INSERT INTO `Rewards` (`PromoID`, `Discount`, `ValidThrough`, `MembershipLevel`)"
                    + "VALUES(?,?,?,?);";
            prepAddDiscount = conn.prepareStatement(sql);

            sql = "DELETE FROM `Rewards` WHERE PromoID = ?;";
            prepDeleteDiscount = conn.prepareStatement(sql);

            sql = "UPDATE `Rewards` SET `Discount` = ?, `ValidThrough` = ?, `MembershipLevel` = ?"
                    + "WHERE PromoID = ?;";
            prepUpdateDiscount = conn.prepareStatement(sql);

            //Merchandise Table
            sql = "INSERT INTO `Merchandise` (`ProductID`, `ProductName`, `SupplierID`, `Quantity`, `BuyPrice`, `MarketPrice`,`ManufactureDate`,`ExpirationDate`)"
                    + "VALUES(?,?,?,?,?,?,?,?);";
            prepAddMerchandise = conn.prepareStatement(sql);

            sql = "DELETE FROM `Merchandise` WHERE ProductID = ?;";
            prepDeleteDiscount = conn.prepareStatement(sql);

            sql = "UPDATE `Merchandise` SET `ProductName`= ?, `SupplierID`= ?, `Quantity`= ?, `BuyPrice`= ?, `MarketPrice`= ?,`ManufactureDate`= ?,`ExpirationDate`= ?"
                    + "WHERE ProductID = ?;";
            prepUpdateMerchandise = conn.prepareStatement(sql);

            //Staff Table
            sql="INSERT INTO `StaffMember` (`StaffID`, `StoreID`, `Name`, `Age`, `Address`, `JobTitle` , `PhoneNumber`, `Email`, `JoiningDate` )"
                    + "VALUES(?,?,?,?,?,?,?,?,?);";
            prepAddStaff = conn.prepareStatement(sql);

            sql = "DELETE FROM `StaffMember` WHERE StaffID = ?;";
            prepDeleteStaff = conn.prepareStatement(sql);

            sql = "UPDATE `StaffMember` SET `StoreID` = ?, `Name` = ?, `Age` = ?, `Address`= ?, `JobTitle` = ? , `PhoneNumber` = ?, `Email` = ?, `JoiningDate` = ? "
                    + "WHERE StaffID = ?;";
            prepUpdateStaff = conn.prepareStatement(sql);

            //Transaction

            sql="INSERT INTO `Transaction` (`TransactionID`, `StoreID`, `CustomerID`, `CashierID`, `PurchaseDate`, `TotalPrice` )"
                    + "VALUES(?,?,?,?,?,?);";
            prepAddTransaction = conn.prepareStatement(sql);

            sql = "DELETE FROM `Transaction` WHERE TransactionID = ?;";
            prepDeleteTransaction = conn.prepareStatement(sql);

            sql = "UPDATE `StaffTransaction` SET `StoreID` = ?, `CustomerID` = ?, `CashierID` = ?, `PurchaseDate` = ?, `TotalPrice` =? "
                    + "WHERE TransactionID = ?;";
            prepUpdateTransaction = conn.prepareStatement(sql);

            //Club Member
            sql = "UPDATE `ClubMember` SET `ActiveStatus` = ?, `Name` = ?, `Address` = ?, `Phone` = ?, `Email` = ? "
                    + "WHERE CustomerID = ?;";
            prepUpdateCustomer = conn.prepareStatement(sql);

            sql = "SELECT CustomerID AS customerid, ActiveStatus as activestatus, Name as name, Address as address, Phone as phone, Email as email FROM ClubMember WHERE Name LIKE ?;";
            prepGetCustomer = conn.prepareStatement(sql);

            //Store
            sql = "INSERT INTO `Store` (`ManagerID`, `StoreAddress`, `PhoneNumber`)"
                    + "VALUES(?,?,?);";
            prepAddStore = conn.prepareStatement(sql);

            sql = "DELETE FROM `Store` WHERE StoreID = ?;";
            prepDeleteStore = conn.prepareStatement(sql);
            
            sql = "SELECT StaffID as staffid FROM StaffMember WHERE Name like ?;";
            prepGetManager = conn.prepareStatement(sql);

            sql = "Select Store.StoreID as storeid, StaffMember.Name as manager, Store.StoreAddress as address, Store.PhoneNumber as phone "
                + "FROM Store INNER JOIN StaffMember ON Store.ManagerID = StaffMember.StaffID;";
            prepGetStores = conn.prepareStatement(sql);

            sql = "Select * FROM Store WHERE StoreId = ?;";
            prepGetStore = conn.prepareStatement(sql);

            sql = "UPDATE Store SET ? = ? WHERE StoreID = ?;";
            prepUpdateStore = conn.prepareStatement(sql);
            
        } catch (SQLException e) {
			e.printStackTrace();
		}
    }

    public static void addDiscount(String promoID, String discount, String validThrough, String membershipLevel) {
        try {
            conn.setAutoCommit(false);
            try{
                prepAddDiscount.setString(1,promoID);
                prepAddDiscount.setBigDecimal(2,new BigDecimal(discount));
                prepAddDiscount.setDate(3,java.sql.Date.valueOf(validThrough));
                prepAddDiscount.setString(4,membershipLevel);

                prepAddDiscount.executeUpdate();
                conn.commit();
            }catch (SQLException e) {
				conn.rollback();
				e.printStackTrace();
            } finally {
				conn.setAutoCommit(true);
			}
        }catch (SQLException e) {
			e.printStackTrace();
		}
    }

    public static void deleteDiscount(String promoID) {
		try {
			conn.setAutoCommit(false);
			try {
				prepDeleteDiscount.setString(1, promoID);
				prepDeleteDiscount.executeUpdate();
				conn.commit();
			} catch (SQLException e) {
				conn.rollback();
				e.printStackTrace();
			} finally {
				conn.setAutoCommit(true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

    /*public static void updateRewards(String promoID) {
        Scanner sc = new Scanner(System.in);
        String sql = "SELECT * from `Rewards` where PromoID="+promoID;
        PreparedStatement read = conn.prepareStatement(sql);
        String membershiplevel;
        String validThrough;
        BigDecimal discount;

		ResultSet rs = read.executeQuery();
        try{
            membershiplevel = rs.getString("MembershipLevel");
            validThrough = rs.getDate("ValidThrough").toString();
            discount = rs.getBigDecimal("Discount");
        } catch (SQLException e) {
			e.printStackTrace();
		}
        int option = 0;
        while(option != 100) {
            System.out.println("1 - Update Discount");
			System.out.println("2 - Update Valid Through");
			System.out.println("3 - Update Membership Level");
            System.out.println("100 - Confirm");
            option = sc.nextInt();
            switch(option){
                case 1: System.out.println("Enter the Discount");
                        discount = new BigDecimal(sc.next());
                        break;
                case 2: System.out.println("Enter the Valid Through(yyyy-mm-dd)");
                        validThrough = sc.next();
                        break;
                case 3: System.out.println("Enter the Membership Level");
                        membershiplevel = sc.next();
                        break;
                default:
                        break; 
            }
        }
        try {
			conn.setAutoCommit(false);
			try {
				prepUpdateDiscount.setBigDecimal(1, discount);
                prepUpdateDiscount.setDate(2, java.sql.Date.valueOf(validThrough));
                prepUpdateDiscount.setString(3, membershiplevel);
                prepUpdateDiscount.setString(4, promoID);
				prepUpdateDiscount.executeUpdate();
				conn.commit();
			} catch (SQLException e) {
				conn.rollback();
				e.printStackTrace();
			} finally {
				conn.setAutoCommit(true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

    } */
    
    public static void addMerchandise(String productID, String productName, String supplierID, String quantity, String buyPrice, String marketPrice, String manufactureDate, String expirationDate) {
        try {
            conn.setAutoCommit(false);
            try{
                prepAddMerchandise.setString(1,productID);
                prepAddMerchandise.setString(2,productName);
                prepAddMerchandise.setString(3,supplierID);
                prepAddMerchandise.setInt(4,Integer.valueOf(quantity));
                prepAddMerchandise.setBigDecimal(5, new BigDecimal(buyPrice));
                prepAddMerchandise.setBigDecimal(6, new BigDecimal(marketPrice));
                prepAddMerchandise.setDate(7,java.sql.Date.valueOf(manufactureDate));
                prepAddMerchandise.setDate(8,java.sql.Date.valueOf(expirationDate));

                prepAddMerchandise.executeUpdate();
                conn.commit();
            }catch (SQLException e) {
				conn.rollback();
				e.printStackTrace();
            } finally {
				conn.setAutoCommit(true);
			}
        }catch (SQLException e) {
			e.printStackTrace();
		}
    }

    public static void deleteMerchandise(String productID) {
		try {
			conn.setAutoCommit(false);
			try {
				prepUpdateMerchandise.setString(1, productID);
				prepUpdateMerchandise.executeUpdate();
				conn.commit();
			} catch (SQLException e) {
				conn.rollback();
				e.printStackTrace();
			} finally {
				conn.setAutoCommit(true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
    
    /*public static void addStaff(String StaffID, String StoreID, String Name, String Age, String Address, String JobTitle , String PhoneNumber, String Email, String JoiningDate) {
        try {
            conn.setAutoCommit(false);
            try{
                prepAddStaff.setInt(1,StaffID);
                prepAddStaff.setString(2,StoreID);
                prepAddStaff.setString(3,Name);
                prepAddStaff.setString(4, Integer.parseInt(Age));
                prepAddStaff.setString(5,Address);
                prepAddStaff.setString(6,JobTitle);
                prepAddStaff.setString(7,PhoneNumber);
                prepAddStaff.setString(8, Email);
                prepAddStaff.setDate(9,java.sql.Date.valueOf(JoiningDate));

                prepAddStaff.executeUpdate();
                conn.commit();
            }catch (SQLException e) {
				conn.rollback();
				e.printStackTrace();
            } finally {
				conn.setAutoCommit(true);
			}
        }catch (SQLException e) {
			e.printStackTrace();
		}
    }*/

    public static void deleteStaff(String StaffID) {
		try {
			conn.setAutoCommit(false);
			try {
				prepDeleteStaff.setString(1, StaffID);
				prepDeleteStaff.executeUpdate();
				conn.commit();
			} catch (SQLException e) {
				conn.rollback();
				e.printStackTrace();
			} finally {
				conn.setAutoCommit(true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

   /* public static void updateStaff(String StaffID) {
        Scanner sc = new Scanner(System.in);
        String sql = "SELECT * from `StaffMember` where StaffID="+StaffID;
        PreparedStatement read = conn.prepareStatement(sql); 
		ResultSet rs = read.executeQuery();

        String StoreID ;
        String Name ;
        Integer Age;
        String Address ;
        String JobTitle ;   
        String PhoneNumber ;
        String Email ;  
        String JoiningDate ;

        

        try{
            StoreID = rs.getString("StoreID");
            Name = rs.getString("Name");
            Age = rs.getInt("Age");
            Address = rs.getString("Address");
            JobTitle = rs.getString("JobTitle");   
            PhoneNumber = rs.getString("PhoneNumber");
            Email = rs.getString("Email");  
            JoiningDate = rs.getDate("JoiningDate").toString();

        } catch (SQLException e) {
			e.printStackTrace();
		}
        int option = 0;
        while(option != 100) {
            System.out.println("1 - Update StoreID");
			System.out.println("2 - Update Name");
			System.out.println("3 - Update Age");
            System.out.println("4 - Update Address");
			System.out.println("5 - Update Job Title");
            System.out.println("6 - Update Phone number");
			System.out.println("7 - Update Email ID ");
			System.out.println("8 - Update Joining Date");



            System.out.println("100 - Confirm");
            option = sc.nextInt();
            switch(option){
                case 1: System.out.println("Enter the StoreID");
                        StoreID = new sc.next();
                        break;
                case 2: System.out.println("Enter the Name");
                        Name = sc.next();
                        break;
                case 3: System.out.println("Enter the Age");
                        Age = Integer(sc.next());
                        break;
                case 4: System.out.println("Enter the Address");
                        Address = sc.next();
                        break;
                case 5: System.out.println("Enter the job tite");
                        JobTitle = sc.next();
                        break;
                case 6: System.out.println("Enter the phone number");
                        PhoneNumber = sc.next();
                        break;
                case 7: System.out.println("Enter the email address");
                        Email = sc.next();
                        break;
                case 8: System.out.println("Enter the joining date (yyyy-mm-dd)");
                        JoiningDate = sc.next();
                        break;
                default:
                        break; 
            }
        }
        try {
			conn.setAutoCommit(false);
			try {
                
                prepUpdateStaff.setString(1,StoreID);
                prepUpdateStaff.setString(2,Name);
                prepUpdateStaff.setString(3, Integer.parseInt(Age));
                prepUpdateStaff.setString(4,Address);
                prepUpdateStaff.setString(5,JobTitle);
                prepUpdateStaff.setString(6,PhoneNumber);
                prepUpdateStaff.setString(7, Email);
                prepUpdateStaff.setDate(8,java.sql.Date.valueOf(JoiningDate));
                prepUpdateStaff.setInt(9,StaffID);
				prepUpdateStaff.executeUpdate();
				conn.commit();
			} catch (SQLException e) {
				conn.rollback();
				e.printStackTrace();
			} finally {
				conn.setAutoCommit(true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

    }*/
    
    public static void enterStoreInfo() {
        // "INSERT INTO `Store` (`ManagerID`, `StoreAddress`, `PhoneNumber`) VALUES(?,?,?);";
        int managerId = -1;
        String managerName = "";
        String address = "";
        String phone = "";
        boolean managerFound = false;

        System.out.println("Beginning new store registration. Enter the following information:");
        
        Scanner in = new Scanner(System.in);

        System.out.println("Address:");
        address = in.nextLine();

        System.out.println("Phone number:");
        phone = in.nextLine();

        // Based on manager's name, get their staff id
        // Name needs to be an existing staff member's name
        while (!managerFound) {
            System.out.println("Manager name:");
            managerName = in.nextLine();

            try (Statement stmt = conn.createStatement()){
                prepGetManager.setString(1, managerName);
                ResultSet rs = prepGetManager.executeQuery();
                
                if (rs.next() == false) {
                    System.out.println("That's not a manager in the system. Please try again");
                } else {
                    managerId = rs.getInt("staffid");
                    managerFound = true;
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
        // "INSERT INTO `Store` (`ManagerID`, `StoreAddress`, `PhoneNumber`) VALUES(?,?,?);";
        // Enter the new store info
        try {
            prepAddStore.setInt(1, managerId);
            prepAddStore.setString(2, address);
            prepAddStore.setString(3, phone);

            prepAddStore.executeUpdate();
            System.out.println("Store added successfully");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    
    public static void deleteStore() {
        // Print all the active stores
        // User enters store id of the store they want to delete
        // Execute SQL to delete the store

        // Deletes tuple from Store and sets corresponding StaffMember.StoreID to null

        int storeId = -1;
        boolean validStoreId = false;

        System.out.println("From the below list of stores, enter the store id for the store to delete:");
        getStores();
        Scanner in = new Scanner(System.in);

        try {
       
            // Make sure user enters a valid store id
            while (!validStoreId) {
                storeId = in.nextInt();
                prepGetStore.setInt(1, storeId);
                ResultSet rs = prepGetStore.executeQuery();
    
                if (rs.next() == false) {
                    System.out.println("That's not a manager in the system. Please try again");
                } else {
                    storeId = rs.getInt("storeid");
                    validStoreId = true;
                }
            }

            prepDeleteStore.setInt(1, storeId);
            prepDeleteStore.executeUpdate();
            System.out.println("Store deleted successfully");

        } catch (SQLException e) {System.out.println(e);}
        
    }
    
    public static void getStores(){
        try {

            ResultSet rs = prepGetStores.executeQuery();
            // Select s.StoreID as storeid, m.Name as manager, s.StoreAddress as address, s.PhoneNumber as phone"
            while (rs.next()) {
                System.out.print(rs.getString("storeid") + " | ");
                System.out.print(rs.getString("manager") + " | ");
                System.out.print(rs.getString("address") + " | ");
                System.out.print(rs.getString("phone") + "\n");
            }
        } catch (SQLException e) {System.out.println(e);}

    }

    public static void updateStore() {
        int updatedAttribute = -1;
        int storeId = -1;
        boolean validStoreId = false;
        boolean validInput = false;

        // Ask for storeid for store to be updated
        // Display all stores
        System.out.println("Enter a store id from the stores below to update");
        getStores();
        in = new Scanner(System.in);

        try {
       
            // Make sure user enters a valid store id
            while (!validStoreId) {
                storeId = in.nextInt();
                prepGetStore.setInt(1, storeId);
                ResultSet rs = prepGetStore.executeQuery();
    
                if (rs.next() == false) {
                    System.out.println("That's not a valid store id. Please try again");
                } else {
                    storeId = rs.getInt("storeid");
                    validStoreId = true;
                }
            }
        } catch (SQLException e) {System.out.println(e);}

        try {
            while (!validInput) {
                // Ask which attribute to update
                System.out.println("Choose which attribute to update:");
                System.out.println("1 - Store address\n2 - Phone number");
                updatedAttribute = in.nextInt();

                if (updatedAttribute == 1) {
                    System.out.println("Enter new address:");
                    String address = in.nextLine();
                    prepUpdateStore.setString(1, "StoreAddress");
                    prepUpdateStore.setString(2, address);
                    prepUpdateStore.setInt(3, storeId);
                    prepUpdateStore.executeUpdate();
                    validInput = true;
                    System.out.println("Address updated successfully");
                } else if (updatedAttribute ==2){

                    System.out.println("Enter new phone number");
                    String phone = in.nextLine();
                    prepUpdateStore.setString(1, "PhoneNumber");
                    prepUpdateStore.setString(2, phone);
                    prepUpdateStore.setInt(3, storeId);
                    prepUpdateStore.executeUpdate();
                    validInput = true;
                    System.out.println("Phone number updated successfully");
                }
                else {
                    System.out.println("Not a valid option, try again");
                }
            }
        } catch (Exception e) {System.out.println(e);}


        // Execute update
    }
    public static void main(String[] args) {
        
        // Setup db connection w username and password
        System.out.println("Connecting to database...");
        conn = getConnection();
        // TO DO if connection can't be made then try again or exit

        generatePreparedStatement();
        // Create db and tables- loop through commands and execute each
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
                    if (input > 0 && input <6){
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

                        case 3:
                            updateMember();
                        break;
                        // To do: Build out remaining options
                    }
                break;

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
                break;

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
                break;

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

                        // Create a new store
                        case 2:
                            enterStoreInfo();
                        break;

                        // Delete a store
                        case 3:
                            deleteStore();
                        break;

                        case 4:
                            updateStore();
                        break;
                    }
                break;

                default:
                break;
            }
        }
        try {
            in.close();
            conn.close();
        }
        catch (Exception e) {
            System.out.println(e);
        }
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
            System.out.println("\t3 - Update an existing member's information");
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
            System.out.println("\t2 - Add a new store");
            System.out.println("\t3 - Delete a store");
            System.out.println("\t4 - Update a store's information");
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

        query = String.format("INSERT INTO ClubMember (ActiveStatus,Name,MembershipLevel,Address,Phone,Email) VALUES (\"Active\", \"%s\", \"Standard\", \"%s\", \"%s\", \"%s\")", name, address, phone, email);

        try (Statement stmt = con.createStatement()){
            stmt.executeQuery(query);
            System.out.println("Member added successfully");
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println(query);
        }

    }

    public static void updateMember() {
        int customerId = 0;
        int activeStatusOption = -1;
        String activeStatus = "";
        String name = "";
        String address = "";
        String phone = "";
        String email = "";
        int updatedAttribute = -1;

        System.out.println("Enter the name of the user to be updated");
        Scanner in = new Scanner(System.in);
        name = in.nextLine();

        // Get the customerid for user to be updated
        try (Statement stmt = conn.createStatement()){
            prepGetCustomer.setString(1, name);
            ResultSet rs = prepGetCustomer.executeQuery();
            while (rs.next()) {
                // Store all the current values
                // Might be overriden based on user input
                customerId = rs.getInt("customerid");
                activeStatus = rs.getString("activestatus");
                name = rs.getString("name");
                address = rs.getString("address");
                phone = rs.getString("phone");
                email = rs.getString("email");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        System.out.println("Choose which data to update:");
        System.out.println("1 - Active status\n2 - Name\n3 - Address\n4 - Phone number\n5 - Email address");
        updatedAttribute = in.nextInt();

        switch(updatedAttribute) {
            case 1:
                while (activeStatusOption != 0 && activeStatusOption != 1) {
                    System.out.println("Enter 1 to set this user to active and 0 for inactive");
                    activeStatusOption = in.nextInt();
                }
                if (activeStatusOption == 1) {
                    activeStatus = "Active";
                }
                else {
                    activeStatus = "Inactive";
                }
                break;
            case 2:
                System.out.println("Enter users updated name");
                in.nextLine();
                name = in.nextLine();
                break;
            case 3:
                System.out.println("Enter users updated address");
                in.nextLine();
                address = in.nextLine();
                break;
            case 4:
                System.out.println("Enter users updated phone number");
                in.nextLine();
                phone = in.nextLine();
                break;
            case 5:
                System.out.println("Enter users updated email address");
                in.nextLine();
                address = in.nextLine();
                break;
        }

        // "UPDATE `ClubMember` SET `ActiveStatus` = ?, `Name` = ?, `Address` = ?, `Phone` = ?, `Email` = ? WHERE CustomerID = ?;";

        try {
            conn.setAutoCommit(false);
            try{
                prepUpdateCustomer.setString(1, activeStatus);
                prepUpdateCustomer.setString(2, name);
                prepUpdateCustomer.setString(3, address);
                prepUpdateCustomer.setString(4, phone);
                prepUpdateCustomer.setString(5, email);
                prepUpdateCustomer.setInt(6, customerId);

                prepUpdateCustomer.executeUpdate();
                conn.commit();
                System.out.println("Member information updated successfully");
            } catch (SQLException e) {
				conn.rollback();
				e.printStackTrace();
            } finally {
				conn.setAutoCommit(true);
			}
        }catch (SQLException e) {
			e.printStackTrace();
		}
    }
}