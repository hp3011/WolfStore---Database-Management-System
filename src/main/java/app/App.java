/*
 * CSC540 Project Application
 * Team K
 * After program starts, it create tables in the database and populate them with demo data
 * User interface:
 * Which Department You Belong To?
 
2 - Registration Staff
	-- Reg Staff APIs
3 - BillingExecutive
	-- Manage Billing
4 - WarehouseOperator
	-- Manager Inventory
5 - Admin
	-- Manager of all APIs
0 - QUIT
	-- exit the program

Function documentations:
	connectToDatabase function: establish connection
	Show Options: show available commands
	generate... functions: generate prepared statements and tables
	Load Data... functions: populate db tables when the program starts
	main function: print welcome message, show available commands, take input, execute query, close and quit

 
	1. Prepared Statement: We used Prepared Statements to make our code much more clear and efficient. 
	2. Uniqueness, primary key, and foreign key constraints: To maintain consistency, we used these constraints and reduced redundancy. 
	3. Transactions: We used Transactions to ensure the data integrity of the database.
	4. UI: We created a UI which is very user friendly and easy to understand.
	5. String parameters: We have taken in String parameters as input from the user and then converted them to specific types.	
	6. Check/Assertion in both SQL and Application Level: We have checked and Asserted some applications in both SQL and Code level. 
	   For example, we check if a query statement return nothing in our application code with the help of "ResultSet" in JAVA while we make sure that the returned items
	   satisfy constraints such as "CustomerID IS NOT NULL" in a SQL statement.
	7. Unified name of functions and of variables: We ensured a single format for function names throughout the code. 

*/



package main.java.app;
import java.math.BigDecimal;
import java.sql.*;  
import java.util.*;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;

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
    static Pattern monthYear = Pattern.compile("\\d{4}/\\d{2}");
    private static Scanner in;
    static  int a= 1;
    static int shipmentIDCounter = 1; 

    //Global Definetions
    private static PreparedStatement prepAddDiscount;
    private static PreparedStatement prepUpdateDiscount;
    private static PreparedStatement prepDeleteDiscount;
    private static PreparedStatement prepGetRewards;
    private static PreparedStatement prepGetPromoID;
    private static PreparedStatement prepAddRewardsEligible;
    private static PreparedStatement prepGetCustomerPromoID;
    private static PreparedStatement prepUpdateRewardsEligible;
    private static PreparedStatement prepGetCustomerID;

    private static PreparedStatement prepUpdateCustomer;
    private static PreparedStatement prepGetCustomer;

    private static PreparedStatement prepAddCustomerPaysBill;
    private static PreparedStatement prepDeleteCustomerPaysBill;

    private static PreparedStatement prepGetProductList;

    private static PreparedStatement prepAddMerchandise;
    private static PreparedStatement prepUpdateMerchandise;
    private static PreparedStatement prepDeleteMerchandise;
    private static PreparedStatement prepGetMerchandise;

    private static PreparedStatement prepAddTransaction;
    private static PreparedStatement prepUpdateTransaction;
    private static PreparedStatement prepDeleteTransaction;

    private static PreparedStatement prepAddPurchasedItems;
    private static PreparedStatement prepUpdatePurchasedItems;
    private static PreparedStatement prepDeletePurchasedItems;

    private static PreparedStatement prepAddStaff;
    private static PreparedStatement prepUpdateStaff;
    private static PreparedStatement prepDeleteStaff;

    private static PreparedStatement prepAddSupplier;
    private static PreparedStatement prepDeleteSupplierEntry;
    private static PreparedStatement prepUpdateSupplierSpecificEntry;

    private static PreparedStatement prepGetQuantityStoreStock;
    private static PreparedStatement prepAddShipment;
    private static PreparedStatement prepDeleteShipment;
    private static PreparedStatement prepUpdateShipment;
    private static PreparedStatement prepAddCustomerShipment;
    private static PreparedStatement prepDeleteCustomerShipment;
    private static PreparedStatement prepAddSupplierShipment;
    private static PreparedStatement prepDeleteSupplierShipment;
    private static PreparedStatement prepAddStoreShipment;
    private static PreparedStatement prepDeleteStoreShipment;
    private static PreparedStatement prepGetShipment;

    private static PreparedStatement prepAddShipmentConsistsOf;
    private static PreparedStatement prepDeleteShipmentConsistsOf;
    private static PreparedStatement prepGetShipmentConsistsOf;
    private static PreparedStatement prepGetSupplier;



    private static PreparedStatement prepGetManager;
    private static PreparedStatement prepGetStaff;	 
    private static PreparedStatement prepAddStore;
    private static PreparedStatement prepGetStores;
    private static PreparedStatement prepGetStore;
    private static PreparedStatement prepDeleteStore;
    private static PreparedStatement prepUpdateStore;
    private static PreparedStatement prepUpdateStorePhone;
    private static PreparedStatement prepUpdateStoreAddress;

    private static PreparedStatement prepGetStoreStock;
    private static PreparedStatement prepAddStoreStock;
    private static PreparedStatement prepUpdateStoreStock;
    private static PreparedStatement prepDeleteStoreStock;
    private static PreparedStatement prepGetProductStoreStock;

    private static PreparedStatement prepGetPrice;
    private static PreparedStatement prepGetDiscount;

    private static PreparedStatement prepCustomerReport;

    private static PreparedStatement prepAddCashier;
    private static PreparedStatement prepAddBillingExecutive;
    private static PreparedStatement prepAddAdmin;
    private static PreparedStatement prepAddWarehouseOperator;
    private static PreparedStatement prepAddRegistrationStaff;
    private static PreparedStatement prepDeleteCashier;
    private static PreparedStatement prepDeleteBillingExecutive;
    private static PreparedStatement prepDeleteAdmin;
    private static PreparedStatement prepDeleteWarehouseOperator;
    private static PreparedStatement prepDeleteRegistrationStaff;

    private static PreparedStatement prepAddReport;
    private static PreparedStatement prepAddShipmentBill;
    private static PreparedStatement prepGetSupShipment; 
    private static PreparedStatement perpCreateStoreSalesGrowthReport;
    private static PreparedStatement perpTotalSalesYear; 
    private static PreparedStatement perpTotalSalesMonth;
    private static PreparedStatement perpTotalSalesDate;
    private static PreparedStatement perpCreateCustomerActivityReport;
 
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

            sql = "SELECT * from `Rewards` WHERE PromoID = ?;";
            prepGetRewards = conn.prepareStatement(sql);

            sql = "SELECT PromoID from `Rewards` WHERE MembershipLevel = ?;";
            prepGetPromoID = conn.prepareStatement(sql);

            sql = "INSERT INTO `RewardsEligibleFor` (`CustomerID`, `PromoID`) VALUES(?,?);";
            prepAddRewardsEligible = conn.prepareStatement(sql);

            sql = "SELECT PromoID from `RewardsEligibleFor` WHERE CustomerID = ?;";
            prepGetCustomerPromoID = conn.prepareStatement(sql);

            sql = "UPDATE `RewardsEligibleFor` SET PromoID = ? WHERE CustomerID = ?;";
            prepUpdateRewardsEligible = conn.prepareStatement(sql);

            sql = "SELECT * from `ClubMember` WHERE CustomerID = (SELECT max(CustomerID) from `ClubMember`);";
            prepGetCustomerID = conn.prepareStatement(sql);

            //Merchandise Table
            sql = "INSERT INTO `Merchandise` (`ProductID`, `ProductName`, `SupplierID`, `Quantity`, `BuyPrice`, `MarketPrice`,`ManufactureDate`,`ExpirationDate`, `IsOnSale`)"
                    + "VALUES(?,?,?,?,?,?,?,?,?);";
            prepAddMerchandise = conn.prepareStatement(sql);

            sql = "DELETE FROM `Merchandise` WHERE ProductID = ?;";
            prepDeleteMerchandise = conn.prepareStatement(sql);

            sql = "UPDATE `Merchandise` SET `ProductName`= ?, `SupplierID`= ?, `Quantity`= ?, `BuyPrice`= ?, `MarketPrice`= ?,`ManufactureDate`= ?,`ExpirationDate`= ?, `IsOnSale`=?"
                    + " WHERE ProductID = ?;";
            prepUpdateMerchandise = conn.prepareStatement(sql);

            sql = "SELECT * from `Merchandise` WHERE ProductID = ?;";
            prepGetMerchandise = conn.prepareStatement(sql);

            //CustomerPaysBillTo
            sql = "INSERT INTO `CustomerPaysBillTo` (`CustomerID`, `TransactionID`) VALUES(?,?);";
            prepAddCustomerPaysBill = conn.prepareStatement(sql);

            sql = "DELETE FROM `CustomerPaysBillTo` WHERE TransactionID = ?;";
            prepDeleteCustomerPaysBill = conn.prepareStatement(sql);

            sql = "SELECT * from `PurchasedItems` WHERE TransactionID = ?;";
            prepGetProductList = conn.prepareStatement(sql);

            // TransferProduct
            sql = "SELECT Stock from `StoreStock` WHERE StoreID = ? AND ProductID = ?;";
            prepGetQuantityStoreStock = conn.prepareStatement(sql);

            //Staff Table
            sql="INSERT INTO `StaffMember` (`StaffID`, `StoreID`, `Name`, `Age`, `Address`, `JobTitle` , `PhoneNumber`, `Email`, `JoiningDate` )"
                    + "VALUES(?,?,?,?,?,?,?,?,?);";
            prepAddStaff = conn.prepareStatement(sql);

            sql = "DELETE FROM `StaffMember` WHERE StaffID = ?;";
            prepDeleteStaff = conn.prepareStatement(sql);

            sql = "UPDATE `StaffMember` SET `StoreID` = ?, `Name` = ?, `Age` = ?, `Address`= ?, `JobTitle` = ? , `PhoneNumber` = ?, `Email` = ?, `JoiningDate` = ? "
                    + "WHERE StaffID = ?;";
            prepUpdateStaff = conn.prepareStatement(sql);
	    sql = "SELECT * from `StaffMember`"+" where StaffID = ?;";
	    prepGetStaff = conn.prepareStatement(sql);	
            //Transaction

            sql="INSERT INTO `Transaction` (`TransactionID`, `StoreID`, `CustomerID`, `CashierID`, `PurchaseDate`, `TotalPrice` )"
                    + "VALUES(?,?,?,?,?,?);";
            prepAddTransaction = conn.prepareStatement(sql);

            sql = "DELETE FROM `Transaction` WHERE TransactionID = ?;";
            prepDeleteTransaction = conn.prepareStatement(sql);

            sql = "UPDATE `StaffTransaction` SET `StoreID` = ?, `CustomerID` = ?, `CashierID` = ?, `PurchaseDate` = ?, `TotalPrice` =? "
                    + "WHERE TransactionID = ?;";
            prepUpdateTransaction = conn.prepareStatement(sql);

            sql = "SELECT MarketPrice,IsOnSale FROM `Merchandise` WHERE `ProductID` = ?;";
	    prepGetPrice = conn.prepareStatement(sql);

	    sql = "SELECT Discount, ValidThrough FROM `Rewards` WHERE `PromoID` = ?;";
            prepGetDiscount = conn.prepareStatement(sql);
            //Purchased Items

            sql="INSERT INTO `PurchasedItems` (`TransactionID`, `ProductID`, `Quantity` )"
                    + "VALUES(?,?,?);";
            prepAddPurchasedItems = conn.prepareStatement(sql);

            sql = "DELETE FROM `PurchasedItems` WHERE TransactionID = ? ;";
            prepDeletePurchasedItems = conn.prepareStatement(sql);

            sql = "UPDATE `PurchasedItems` SET `Quantity` = ? "
                    + "WHERE TransactionID = ? and  ProductID= ? ;";
            prepUpdatePurchasedItems = conn.prepareStatement(sql);


            //Club Member
            sql = "UPDATE `ClubMember` SET `ActiveStatus` = ?, `Name` = ?, `Address` = ?, `Phone` = ?, `Email` = ?, `MembershipLevel` = ? "
                    + "WHERE CustomerID = ?;";
            prepUpdateCustomer = conn.prepareStatement(sql);

            sql = "SELECT * FROM ClubMember WHERE CustomerID = ?;";
            prepGetCustomer = conn.prepareStatement(sql);

            // Supplier Table

            sql =   "INSERT INTO `Supplier` (`SupplierID`,`SupplierName`,`PhoneNumber`,`Email`,`Location`)" + "VALUES(?,?,?,?,?);";
            prepAddSupplier = conn.prepareStatement(sql);

            sql = "DELETE FROM `Supplier` WHERE SupplierID = ?;";
            prepDeleteSupplierEntry = conn.prepareStatement(sql);

            sql = "UPDATE `Supplier` SET `SupplierName` = ?, `PhoneNumber` = ?, `Email` = ?, `Location` = ? " +
                  "WHERE SupplierID = ?;";
            prepUpdateSupplierSpecificEntry = conn.prepareStatement(sql);

            sql = "SELECT * from `Supplier` WHERE SupplierID = ?;";
            prepGetSupplier = conn.prepareStatement(sql);

            // Shipment Table

            sql = "INSERT INTO `Shipment` (`ShipmentID`, `Type`, `SentBy`, `ReceivedBy`, `SentDate`, `ReceivedDate`)" + 
            "VALUES (?,?,?,?,?,?);";
            prepAddShipment = conn.prepareStatement(sql);

            sql = "DELETE FROM `Shipment` WHERE ShipmentID = ?;";
            prepDeleteShipment = conn.prepareStatement(sql);

            sql = "UPDATE `Shipment` SET `SentBy` = ?, `ReceivedBy` = ?, `SentDate` = ?, `ReceivedDate` = ? " +
                    "WHERE ShipmentID = ?;";
            prepUpdateShipment = conn.prepareStatement(sql);

            sql = "SELECT * from `Shipment` WHERE ShipmentID = ?;";
            prepGetShipment = conn.prepareStatement(sql);

            sql = "SELECT * from `Shipment` WHERE Type = 'Supplier';";
            prepGetSupShipment = conn.prepareStatement(sql);
            // Customer Shipment Table

            sql = "INSERT INTO `CustomerShipment` (`ShipmentID`) " + " VALUES (?);";
            prepAddCustomerShipment = conn.prepareStatement(sql);

            sql = "DELETE FROM `CustomerShipment` WHERE ShipmentID = ?;";
            prepDeleteCustomerShipment = conn.prepareStatement(sql);

             // Supplier Shipment Table

            sql = "INSERT INTO `SupplierShipment` (`ShipmentID`) " + " VALUES (?);";
            prepAddSupplierShipment = conn.prepareStatement(sql);

            sql = "DELETE FROM `SupplierShipment` WHERE ShipmentID = ?;";
            prepDeleteSupplierShipment = conn.prepareStatement(sql);

             // Store Shipment Table

            sql = "INSERT INTO `StoreShipment` (`ShipmentID`) " + " VALUES (?);";
            prepAddStoreShipment = conn.prepareStatement(sql);

            sql = "DELETE FROM `StoreShipment` WHERE ShipmentID = ?;";
            prepDeleteStoreShipment = conn.prepareStatement(sql);
          

            // Shipment Consists OF table

            sql = "INSERT INTO `ShipmentConsistsOf` (`ShipmentID`, `ProductID`, `Quantity`)" + 
                    "VALUES (?,?,?);";
            prepAddShipmentConsistsOf = conn.prepareStatement(sql);

            sql = "DELETE FROM `ShipmentConsistsOf` WHERE ShipmentID = ?;";
            prepDeleteShipmentConsistsOf = conn.prepareStatement(sql);


            sql = "SELECT * FROM ShipmentConsistsOf where ShipmentID= ?;";
            prepGetShipmentConsistsOf = conn.prepareStatement(sql);

	    // Shipment Bill
	   
            sql = "INSERT INTO `ShipmentBill` (`ShipmentID`, `Amount`, `BillingDate` , `BillingExecutiveID`)" +
                    "VALUES (?,?,?,?);";
            prepAddShipmentBill = conn.prepareStatement(sql);
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
            
            sql = "UPDATE Store SET PhoneNumber = ? WHERE StoreID = ?;";
            prepUpdateStorePhone = conn.prepareStatement(sql);

            sql = "UPDATE Store SET StoreAddress = ? WHERE StoreID = ?;";
            prepUpdateStoreAddress = conn.prepareStatement(sql);

            //StoreStock
            sql = "SELECT * FROM StoreStock WHERE StoreID = ?;";
            prepGetStoreStock = conn.prepareStatement(sql);

            sql = "SELECT StoreID, Stock FROM StoreStock WHERE ProductID = ?;";
            prepGetProductStoreStock = conn.prepareStatement(sql);

            sql = "UPDATE StoreStock SET Stock = ? WHERE StoreID = ? AND ProductID = ?;";
            prepUpdateStoreStock = conn.prepareStatement(sql);

            sql = "INSERT INTO StoreStock (StoreID, ProductID, Stock) VALUES (?,?,?);";
            prepAddStoreStock = conn.prepareStatement(sql);

            sql = "DELETE FROM StoreStock WHERE StoreID = ? AND ProductID = ?;";
            prepDeleteStoreStock = conn.prepareStatement(sql);

            //Reports
            sql = "SELECT SUM(IF(MONTH(SignupDate) = MONTH(?) AND YEAR(SignupDate) = YEAR(?), 1, 0)) AS new_signups,"
                + "COUNT(*) AS total_signups FROM Signup;";
            prepCustomerReport = conn.prepareStatement(sql);

            // Job tables
            sql = "INSERT INTO Cashier(StaffID) VALUES (?);";
            prepAddCashier = conn.prepareStatement(sql);

            sql = "INSERT INTO BillingExecutive(StaffID) VALUES (?);";
            prepAddBillingExecutive = conn.prepareStatement(sql);

            sql = "INSERT INTO Admin(StaffID) VALUES (?);";
            prepAddAdmin = conn.prepareStatement(sql);

            sql = "INSERT INTO RegistrationStaff(StaffID) VALUES (?);";
            prepAddRegistrationStaff = conn.prepareStatement(sql);

            sql = "INSERT INTO WarehouseOperator(StaffID) VALUES (?);";
            prepAddWarehouseOperator = conn.prepareStatement(sql);

            sql = "DELETE FROM Cashier WHERE StaffID = ?;";
            prepDeleteCashier = conn.prepareStatement(sql);

            sql = "DELETE FROM BillingExecutive WHERE StaffID = ?;";
            prepDeleteBillingExecutive = conn.prepareStatement(sql);

            sql = "DELETE FROM Admin WHERE StaffID = ?;";
            prepDeleteAdmin = conn.prepareStatement(sql);

            sql = "DELETE FROM RegistrationStaff WHERE StaffID = ?;";
            prepDeleteRegistrationStaff = conn.prepareStatement(sql);

            sql = "DELETE FROM WarehouseOperator WHERE StaffID = ?;";
            prepDeleteWarehouseOperator = conn.prepareStatement(sql);


	    //Report 
	    sql = "INSERT INTO `Report` ( `ReportType`, `ReportDetail`)"
                    + "VALUES(?,?);";
	    prepAddReport= conn.prepareStatement(sql);
            // Shop Sales Growth Report

            sql = "SELECT SUM(TotalPrice) AS TotalPriceSum FROM Transaction WHERE PurchaseDate >= ? AND PurchaseDate <= ? AND StoreID = ?;";
            perpCreateStoreSalesGrowthReport = conn.prepareStatement(sql);
	 
	    // Total Sales by Date
	    
	    sql = "SELECT SUM(TotalPrice) AS TotalPriceSum FROM Transaction WHERE PurchaseDate = ?;";
            perpTotalSalesDate = conn.prepareStatement(sql);
		 
	    // Total Sales by Year
	    sql = "SELECT SUM(TotalPrice) AS TotalPriceSum FROM Transaction WHERE year(PurchaseDate) = ?;";
            perpTotalSalesYear = conn.prepareStatement(sql);
	    // Total Sales by month
            sql = "SELECT SUM(TotalPrice) AS TotalPriceSum FROM Transaction WHERE month(PurchaseDate) = ? AND year(PurchaseDate) = ?;";
            perpTotalSalesMonth = conn.prepareStatement(sql);	    
            // Customer Activity Report
                        
            sql = "SELECT SUM(TotalPrice) AS TotalPriceSum FROM Transaction WHERE PurchaseDate >= ? AND PurchaseDate <= ? AND CustomerID = ?;";
            perpCreateCustomerActivityReport = conn.prepareStatement(sql);

            
        }catch (SQLException e) {
			e.printStackTrace();
		}
    }

// Total Sales report

public static void generateTotalSalesReport()
{
//This Function Generate Reports for Total Sales by Month, Year, or Day.

    Scanner sc = new Scanner(System.in);
    String totalSales = null;

    System.out.println("Enter Option");
    System.out.println("\t 1-Total Sales Report By Year:");
    System.out.println("\t 2-Total Sales Report By Month:");
    System.out.println("\t 3-Total Sales Report By Date:");
    int option = sc.nextInt();

    if(option==1){

		System.out.println("Enter Year which you want report for: ");
    		String year = sc.next();
		totalSales = null;	


    try {
                conn.setAutoCommit(false);
                try{
                    perpTotalSalesYear.setString(1,year);


                    ResultSet rs = perpTotalSalesYear.executeQuery();

                     if(!rs.next())
                    {
                        System.out.println("No data available for entered values.");
                        return;
                    }
                    else
                    {
                        totalSales = rs.getString("TotalPriceSum");
                        System.out.println("\n\n############################Your Report Is Ready###############################################\n");
                        System.out.println("\tFor Year "+ year + " Total sales were  " + totalSales + " for entered time period." );
                        System.out.println("\n\n###############################################################################################\n");
			String ReportDetail= "For Year "+ year + " Total sales were  " + totalSales + " for entered time period.";
			addReport("Sales",ReportDetail);
                    }


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
    else if(option ==2){

                System.out.println("Enter Year which you want report for: ");
                String year = sc.next();
		System.out.println("Enter Month of year "+year+" which you want report for: ");
		String month= sc.next();
		totalSales=null;

    try {
                conn.setAutoCommit(false);
                try{
                    perpTotalSalesMonth.setString(1,month);
		    perpTotalSalesMonth.setString(2,year);


                    ResultSet rs = perpTotalSalesMonth.executeQuery();

                     if(!rs.next())
                    {
                        System.out.println("No data available for entered values.");
                        return;
                    }
                    else
                    {
                        totalSales = rs.getString("TotalPriceSum");
			System.out.println("\n\n############################Your Report Is Ready###############################################\n");
                        System.out.println("\tFor Month  "+month+"  of year  "+ year + ", Total sales were  " + totalSales + " for entered time period." );
                        System.out.println("\n\n###############################################################################################\n");
			String ReportDetail = "For Month  "+month+"  of year  "+ year + ", Total sales were  " + totalSales + " for entered time period.";
			addReport("Sales", ReportDetail);
                    }


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
    else if(option==3){

                System.out.println("Enter Date which you want report for: ");
                String date = sc.next();
		totalSales = null;

    try {
                conn.setAutoCommit(false);
                try{
                    perpTotalSalesDate.setString(1,date);
                    ResultSet rs = perpTotalSalesMonth.executeQuery();

                     if(!rs.next())
                    {
                        System.out.println("No data available for entered values.");
                        return;
                    }
                    else
                    {
                        totalSales = rs.getString("TotalPriceSum");
                        System.out.println("\n\n############################Your Report Is Ready###############################################\n");
                        System.out.println("\tFor Date  "+ date + ",  Total sales were  " + totalSales + " for entered time period." );
                        System.out.println("\n\n###############################################################################################\n");
			String ReportDetail= "For Date  "+ date + ",  Total sales were  " + totalSales + " for entered time period.";
			addReport("Sales", ReportDetail);
                    }


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

}




//This API is for  Customer Activity Report function

public static void generateCustomerActivityReport()
{
    Scanner sc = new Scanner(System.in);
    String CustomerID = null;
    String fromDate = null;
    String toDate = null;
    String totalSales = null;

    System.out.println("Enter CustomerID for the report ");
    CustomerID = sc.next();
    System.out.println("Enter timeperiod From date the report ");
    fromDate = sc.next();
    System.out.println("Enter timeperiod To date the report ");
    toDate = sc.next();    

    try {         
                conn.setAutoCommit(false);
                try{
                    perpCreateCustomerActivityReport.setString(1,fromDate);
                    perpCreateCustomerActivityReport.setString(2,toDate);
                    perpCreateCustomerActivityReport.setString(3,CustomerID);


                    ResultSet rs = perpCreateCustomerActivityReport.executeQuery();

                     if(!rs.next())
                    {
                        System.out.println("No data available for entered values.");
                        return;
                    }
                    else
                    {
                        totalSales = rs.getString("TotalPriceSum");  
                        System.out.println("\n\n############################Your Report Is Ready###############################################\n");
                        System.out.println("\tFor Customer ID "+ CustomerID + " Total purchase were  " + totalSales + " for entered time period." );
                        System.out.println("\n\n###############################################################################################\n");

			String ReportDetail= "For Customer ID "+ CustomerID + " Total purchase were  " + totalSales + " for entered time period.";
			addReport("Customer Activity", ReportDetail);

                    }                   


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

// Shop Sales Growth Report function

public static void generateShopSalesGrowthReport()
{
    Scanner sc = new Scanner(System.in);
    String stroreID = null;
    String fromDate = null;
    String toDate = null;
    String totalSales = null;

    System.out.println("Enter stroreID for the report ");
    stroreID = sc.next();
    System.out.println("Enter timeperiod From date the report ");
    fromDate = sc.next();
    System.out.println("Enter timeperiod To date the report ");
    toDate = sc.next();    

    try {         
                conn.setAutoCommit(false);
                try{
                    perpCreateStoreSalesGrowthReport.setString(1,fromDate);
                    perpCreateStoreSalesGrowthReport.setString(2,toDate);
                    perpCreateStoreSalesGrowthReport.setString(3,stroreID);


                    ResultSet rs = perpCreateStoreSalesGrowthReport.executeQuery();

                     if(!rs.next())
                    {
                        System.out.println("No data available for entered values.");
                        return;
                    }
                    else
                    {
                        totalSales = rs.getString("TotalPriceSum");  
                        System.out.println("\n\n############################Your Report Is Ready###############################################\n");
                        System.out.println("\tFor Store ID "+ stroreID + " Total sales were  " + totalSales + " for entered time period." );
                        System.out.println("\n\n###############################################################################################\n");
			String ReportDetail= "For Store ID "+ stroreID + " Total sales were  " + totalSales + " for entered time period.";
                        addReport("Store Sales", ReportDetail);

                    }                   


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


    public static void handleReturns(){
        int quantity;
        String productID;
        int yourStore;
        BigDecimal price;
	String returndate;
        Scanner in = new Scanner(System.in);
	String customerid;
	
	System.out.println("\nEnter Customer ID:");
        customerid = in.next();

        System.out.println("\nEnter your storeID:");
        yourStore = in.nextInt();
        System.out.println("\nEnter Return Date:");
        returndate = in.next();

        System.out.println("\nEnter the ProductID which you are returning:");
        productID = in.next();

        System.out.println("\nEnter the quantity for the Product:");
        quantity = in.nextInt();
	int ExistingQuantity = getQuantityStoreStock(yourStore, productID);
	
            if(ExistingQuantity<0){
                addStoreStock(yourStore, productID, quantity);
            }else{
                updateStoreStock(yourStore, productID, (ExistingQuantity + quantity));
            }
            System.out.println("\nSuccess - Products are Returned to the Store");

	price= getPrice(productID,customerid,returndate);	
	BigDecimal price_temp = price.multiply(new BigDecimal(quantity));
        System.out.println("\n\n######################## Refund Initiated ###############################################\n");

	System.out.println("\t Customer Gets Dollar "+price_temp.toString()+" of refund, Collect it from Staff!");	
        System.out.println("\n\n#########################################################################################\n");
	

    }

    // Supplier Functions
   
    //public static void deleteSupplierInfo(String supplierId)
    public static void deleteSupplierInfo()
    {
        System.out.println("Supplier Entry cannot be deleted.");
        // Scanner sc = new Scanner(System.in);
        // String supplierId = null;

        // System.out.println("Enter supplier ID to be deleted ");
        // supplierId = sc.next();

        // int option = 0;

        // try {         
        //             conn.setAutoCommit(false);
        //             try{
        //                 prepDeleteSupplierEntry.setString(1,supplierId);
        //                 prepDeleteSupplierEntry.executeUpdate();
        //                 conn.commit();
        //             }catch (SQLException e) {
        //                 conn.rollback();
        //                 e.printStackTrace();
        //             } finally {
        //                 conn.setAutoCommit(true);
        //             }
        //         }catch (SQLException e) {
        //             e.printStackTrace();
        //         }
    }
    
    public static void updateSupplierinfo()
    //public static void updateSupplierinfo(String supplierId)
    {
        Scanner sc = new Scanner(System.in);
        int option = 0;
        String supplierId = null;

        System.out.println("Enter supplier ID to be Updated ");
        supplierId = sc.nextLine();   

        String SupplierName = null;
        String PhoneNumber = null;
        String Email = null;
        String Location = null;

        try{
            
            prepGetSupplier.setString(1,supplierId);
		    ResultSet rs = prepGetSupplier.executeQuery();

            if(!rs.next())
            {
                System.out.println("Invalid supplier ID =>  "+ supplierId);
                return;
            }
            else
            {
                SupplierName = rs.getString("SupplierName");                  
                PhoneNumber = rs.getString("PhoneNumber");
                Email = rs.getString("Email");
                Location = rs.getString("Location");   
            }

        } catch (SQLException e) {
			e.printStackTrace();
		}

        while(option != 100)
        {
            System.out.println("1 - Update Supplier name");          
            System.out.println("2 - Update Supplier phone number");
            System.out.println("3 - Update Supplier email id");
            System.out.println("4 - Update Supplier location");

            System.out.println("100 - Confirm\n");

            option =Integer.parseInt(sc.nextLine());            

            switch(option)
            {
                case 1: System.out.println("Enter the Supplier Name");
                        SupplierName =   sc.nextLine();
                        break;

                case 2: System.out.println("Enter the Supplier Phone number");
                        PhoneNumber =   sc.nextLine();
                        break;

                case 3: System.out.println("Enter the Supplier Email");
                        Email =   sc.nextLine();
                        break;

                case 4: System.out.println("Enter the Supplier Location");
                        Location =   sc.nextLine();
                        break;
                default:
                        break;
            }
        }

        try {
			conn.setAutoCommit(false);
			try {
                
                prepUpdateSupplierSpecificEntry.setString(1,SupplierName);
                prepUpdateSupplierSpecificEntry.setString(2,PhoneNumber);
                prepUpdateSupplierSpecificEntry.setString(3,Email);
                prepUpdateSupplierSpecificEntry.setString(4,Location);
                prepUpdateSupplierSpecificEntry.setString(5,supplierId);

				prepUpdateSupplierSpecificEntry.executeUpdate();
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
    


    // public static void enterSupplierinfo(String supplierID, String supplierName, String phoneNumber, String email, String location) {
       public static void enterSupplierinfo() {
        try {

                String supplierID =null;
                String supplierName = null;
                String phoneNumber =null;
                String email = null;
                String location = null;

                Scanner sc = new Scanner(System.in);

                System.out.println("Enter Supplier ID to be entered");
                supplierID =   sc.nextLine();

                System.out.println("Enter the Supplier Name");
                supplierName =   sc.nextLine();

                System.out.println("Enter the Phone Number");
                phoneNumber =   sc.nextLine();

                System.out.println("Enter the Email");
                email =   sc.nextLine();

                System.out.println("Enter the Location");
                location =   sc.nextLine();

               
                


            conn.setAutoCommit(false);
            try{
                prepAddSupplier.setString(1,supplierID);
                prepAddSupplier.setString(2,supplierName);
                prepAddSupplier.setString(3,phoneNumber);
                prepAddSupplier.setString(4,email);
                prepAddSupplier.setString(5,location);

                prepAddSupplier.executeUpdate();
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


    // Shipment Functions

    //public static void updateShipmentinfo(String ShipmentId)
public static void updateShipmentinfo()
{
    Scanner sc = new Scanner(System.in);
    int option = 0;
    String shipmentId =null;
    System.out.println("Enter Shipment ID to be Updated ");
    shipmentId = sc.next();

    String SentBy = null;
    String ReceivedBy = null;
    String SentDate = null;
    String ReceivedDate = null;

    try{
        prepGetShipment.setString(1,shipmentId);
        ResultSet rs = prepGetShipment.executeQuery();

        if(!rs.next())
        {
             System.out.println("Invalid Shipment ID ");
             return;
        }
        SentBy = rs.getString("SentBy");
        ReceivedBy = rs.getString("ReceivedBy");
        SentDate = rs.getString("SentDate");
        ReceivedDate = rs.getString("ReceivedDate");   

    } catch (SQLException e) {
        e.printStackTrace();
    }



    while(option != 100)
    {
        System.out.println("1 - Update SentBy field");          
        System.out.println("2 - Update ReceivedBy field");
        System.out.println("3 - Update SentDate field");
        System.out.println("4 - Update ReceivedDate field");

        System.out.println("100 - Confirm");

        option = sc.nextInt();

        switch(option)
        {
            case 1: System.out.println("Enter the SentBy field");
                    SentBy =   sc.next();
                    break;

            case 2: System.out.println("Enter the ReceivedBy field");
                    ReceivedBy =   sc.next();
                    break;

            case 3: System.out.println("Enter the SentDate field");
                    SentDate =   sc.next();
                    break;

            case 4: System.out.println("Enter the ReceivedDate field");
                    ReceivedDate =   sc.next();
                    break;
            default:
                    break;
        }
    }

    try {
        conn.setAutoCommit(false);
        try {
            
            prepUpdateShipment.setString(1,SentBy);
            prepUpdateShipment.setString(2,ReceivedBy);
            prepUpdateShipment.setString(3,SentDate);
            prepUpdateShipment.setString(4,ReceivedDate);
            prepUpdateShipment.setString(5,shipmentId);


            prepUpdateShipment.executeUpdate();
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


//public static void deleteShipmentInfo(String shipmentId)
public static void deleteShipmentInfo()
{
    Scanner sc = new Scanner(System.in);
    int option = 0;      
    String shipmentType = null;
    String shipmentId = null;

    System.out.println("Enter Shipment ID to be deleted ");
    shipmentId = sc.next();

   
    try{
        prepGetShipment.setString(1,shipmentId);
        ResultSet rs = prepGetShipment.executeQuery();

        if(!rs.next())
        {
             System.out.println("Invalid Shipment ID ");
             return;
        }
       
       
        shipmentType = rs.getString("Type");
    } catch (SQLException e) {
        e.printStackTrace();
    }

    try {
        conn.setAutoCommit(false);
        try{      


            prepDeleteShipmentConsistsOf.setString(1,shipmentId);
            prepDeleteShipmentConsistsOf.executeUpdate();  

            conn.commit();            


            if(shipmentType.equals("customer"))
            {
                prepDeleteCustomerShipment.setString(1,shipmentId);
                prepDeleteCustomerShipment.executeUpdate();
                 conn.commit();

            }
            else if(shipmentType.equals("supplier"))
            {
                prepAddSupplierShipment.setString(1,shipmentId);
                prepAddSupplierShipment.executeUpdate();
                 conn.commit();

            }
            else if(shipmentType.equals("store"))
            {
                prepDeleteStoreShipment.setString(1,shipmentId);
                prepDeleteStoreShipment.executeUpdate();
                conn.commit();    
            }
        

            conn.commit();
        }catch (SQLException e) {
            conn.rollback();
            e.printStackTrace();
        } finally {
            conn.setAutoCommit(true);
        }

        try{
             conn.setAutoCommit(false);
              prepDeleteShipment.setString(1,shipmentId);
            prepDeleteShipment.executeUpdate();
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

//public static void enterShipmentinfo(String shipmentID, String shipmentType, String sentBy, String receivedBy, String sentDate, String receivedDate) {
public static void enterShipmentinfo() {

        String shipmentID = null;
        String shipmentType = null;
        String sentBy = null;
        String receivedBy = null; 
        String sentDate = null; 
        String receivedDate = null;
        String productlist = null;
        String productid = null;
        String quantity = null;
        String[] res;


    try {

         Scanner sc = new Scanner(System.in);


        System.out.println("Enter Shipment ID to be entered");
        shipmentID =   sc.nextLine();

        System.out.println("Enter the Shipment Type");
        shipmentType =   sc.next();

        System.out.println("Enter the Shipment Sent by field value ");
        sentBy =   sc.next();

        System.out.println("Enter the Shipment Received by field value");
        receivedBy =   sc.next();

        System.out.println("Enter the Shipment Sent date");
        sentDate =   sc.next();

        System.out.println("Enter the Shipment Received date");
        receivedDate =   sc.next();

      


        System.out.println("\nEnter the shipment consists of  product ID list with quantity [i.e ITEM_01:3,ITEM_02:3] - \n");
        productlist = in.next();

       res = productlist.split(",");
      
        conn.setAutoCommit(false);
        try{
            prepAddShipment.setString(1,shipmentID);
            prepAddShipment.setString(2,shipmentType);
            prepAddShipment.setString(3,sentBy);
            prepAddShipment.setString(4,receivedBy);
            prepAddShipment.setString(5,sentDate);
            prepAddShipment.setString(6,receivedDate);

            prepAddShipment.executeUpdate();
       
            if(shipmentType.equals("customer"))
            {
                prepAddCustomerShipment.setString(1,shipmentID);
                prepAddCustomerShipment.executeUpdate();                   
              
            }
            else if(shipmentType.equals("supplier"))
            {
                prepAddSupplierShipment.setString(1,shipmentID);
                prepAddSupplierShipment.executeUpdate();
              
            }
            else if(shipmentType.equals("store"))
            {
                prepAddStoreShipment.setString(1,shipmentID);
                prepAddStoreShipment.executeUpdate();
               
            }
              
               conn.commit();

                for(String myStr: res) 
                {
                    String[] array = myStr.split(":");
                    productid = array[0];
                    quantity = array[1];

                    prepAddShipmentConsistsOf.setString(1,shipmentID);
                    prepAddShipmentConsistsOf.setString(2,productid);
                    prepAddShipmentConsistsOf.setString(3,quantity);

                    prepAddShipmentConsistsOf.executeUpdate();
                         
                }
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

//prints cashback for the Platinum customers
public static void generatePlatinumCashback() {
    
    Scanner sc = new Scanner(System.in);
    String customerID = null;
    String fromDate = null;
    String toDate = null;
    String year = null;
    String total = null;
    double cashback = 0.0;
    String membershipLevel = null;

    System.out.println("Enter CustomerID for the report ");
    customerID = sc.next();
    // Checks if the the customer has Platinum Membership or not
    try{
        prepGetCustomer.setString(1, customerID);
        ResultSet rs = prepGetCustomer.executeQuery();
        if(rs.next()){
            membershipLevel = rs.getString("MembershipLevel");
            if(!membershipLevel.equals("Platinum")){
                System.out.println("CustomerID "+ customerID +" is not eligible for yearly 2% Cashback");
                return;
            }
        }
    }catch (SQLException e) {System.out.println(e);}
    System.out.println("Enter the year: ");
    year = sc.next();
    
    fromDate = year+"-01-01";
    toDate = year+"-12-31";
    //Gets the totat price for transaction in particular year and multiply it with 0.02
    try {         
                conn.setAutoCommit(false);
                try{
                    perpCreateCustomerActivityReport.setString(1,fromDate);
                    perpCreateCustomerActivityReport.setString(2,toDate);
                    perpCreateCustomerActivityReport.setString(3,customerID);


                    ResultSet rs = perpCreateCustomerActivityReport.executeQuery();
                    
                    if(!rs.next())
                    {
                        System.out.println("No data available for entered values.");
                        return;
                    }
                    else
                    {
                        total = rs.getString("TotalPriceSum");
                        System.out.println("Total = " + total);
                        cashback = Double.parseDouble(total) * 0.02;
                        System.out.println("\tFor Customer ID "+ customerID + " Cashback for year  " + year + " is " + cashback);
                    }                   
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

   
public static void generateStoreStockReport(){
    Scanner sc = new Scanner(System.in);
    int storeID = 0;

    System.out.println("Enter storeID for the report ");
    storeID = sc.nextInt();

    ResultSet resultSet = getStoreStock(storeID);
    try{
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
	String ReportDetail="";
        System.out.println("\n\n############################Your Report Is Ready###############################################\n");
        System.out.println("Store ID = "+storeID);
        System.out.println("ProductID, Quantity");
	ReportDetail += "Store ID = "+storeID + "ProductID, Quantity";
        while (resultSet.next()) {
            for (int i = 2; i <= columnsNumber; i++) {
                if (i > 2) System.out.print(",      ");
                String columnValue = resultSet.getString(i);
                System.out.print(columnValue);
		ReportDetail += "  "+columnValue;
            }
            System.out.println("");
        }
        System.out.println("\n\n###############################################################################################\n");

	addReport("Store Stock Report",ReportDetail);
    }catch (SQLException e) {System.out.println(e);}

    
}
    
    public static void generateProductStoreStockReport(){
        Scanner sc = new Scanner(System.in);
        String productID = "";

        System.out.println("Enter ProductID for the report ");
        productID = sc.next();

        ResultSet resultSet = getProductStoreStock(productID);
        try{
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            System.out.println("\n\n############################Your Report Is Ready###############################################\n");
            System.out.println("Product ID = "+productID);
            System.out.println("Store ID, Quantity");
            while (resultSet.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",      ");
                    String columnValue = resultSet.getString(i);
                    System.out.print(columnValue);
                }
                System.out.println("");
            }
            System.out.println("\n\n###############################################################################################\n");
        }catch (SQLException e) {System.out.println(e);}

        
    }

    public static ResultSet getProductStoreStock(String productID){
        ResultSet rs = null;
        try {
            conn.setAutoCommit(false);
            try{
                prepGetProductStoreStock.setString(1, productID);

                rs = prepGetProductStoreStock.executeQuery();
                conn.commit();
                return rs;
            }catch (SQLException e) {
				conn.rollback();
				e.printStackTrace();
            } finally {
				conn.setAutoCommit(true);
			}
        }catch (SQLException e) {
			e.printStackTrace();
		}
        return rs;
    }

    //Add the discount information into Rewards Table
    public static void addDiscount() {
        String promoID;
        String discount;
        String validThrough;
        String membershipLevel;

        Scanner in = new Scanner(System.in);

        System.out.println("\nEnter PromoID:");
        promoID = in.next();

        System.out.println("\nEnter Discount:");
        discount = in.next();

        System.out.println("\nEnter validThrough(yyyy-mm-dd):");
        validThrough = in.next();

        System.out.println("\nEnter membershipLevel (Standard/Gold/Platinum):");
        membershipLevel = in.next();

        try {
            conn.setAutoCommit(false);
            try{
                prepAddDiscount.setString(1,promoID);
                prepAddDiscount.setBigDecimal(2,new BigDecimal(discount));
                prepAddDiscount.setDate(3,java.sql.Date.valueOf(validThrough));
                prepAddDiscount.setString(4,membershipLevel);

                prepAddDiscount.executeUpdate();
                conn.commit();
                System.out.println("Success");
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

    //delete the discount information from the rewards table
    public static void deleteDiscount() {
        String promoID;

        Scanner in = new Scanner(System.in);
        System.out.println("\nEnter PromoID to delete:");
        promoID = in.nextLine();
		try {
			conn.setAutoCommit(false);
			try {
				prepDeleteDiscount.setString(1, promoID);
				prepDeleteDiscount.executeUpdate();
				conn.commit();
                System.out.println("Success");
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

    //Update the discount inforamtion to the Rewards table
    public static void updateDiscount() {
        String promoID = "";
        String membershiplevel = "";
        String validThrough = "";
        BigDecimal discount = new BigDecimal(0);
        boolean validPromoID = false;

        Scanner in = new Scanner(System.in);
        System.out.println("\nEnter PromoID:");
        try{
            while(!validPromoID){
                promoID = in.nextLine();

                prepGetRewards.setString(1, promoID);
                ResultSet rs = prepGetRewards.executeQuery();

                if (rs.next() == false) {
                    System.out.println("Invalid PromoID");
                } else {
                    membershiplevel = rs.getString("MembershipLevel");
                    validThrough = rs.getDate("ValidThrough").toString();
                    discount = rs.getBigDecimal("Discount");
                    validPromoID = true;
                }
            }
        } catch (SQLException e) {System.out.println(e);}
        
            

        int option = 0;
        while(option != 100) {
            System.out.println("1 - Update Discount");
			System.out.println("2 - Update Valid Through");
			System.out.println("3 - Update Membership Level");
            System.out.println("100 - Enter to Confirm all the changes");
            option = in.nextInt();
            switch(option){
                case 1: System.out.println("Enter the Discount");
                        discount = new BigDecimal(in.next());
                        break;
                case 2: System.out.println("Enter the Valid Through(yyyy-mm-dd)");
                        validThrough = in.next();
                        break;
                case 3: System.out.println("Enter the Membership Level");
                        membershiplevel = in.next();
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
                System.out.println("Success");
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

    //Returns the promoID for a customer elgible for a discount
    public static String getRewardsEligible(int customerID){
        String customerPromoID = "";

        try{
            prepGetCustomerPromoID.setInt(1, customerID);
            ResultSet rs = prepGetCustomerPromoID.executeQuery();
            if(rs.next()){
                customerPromoID = rs.getString("PromoID");
            }else{
                System.out.println("Error - CustomerId is not Eligible for Rewards");
            }
            
        }catch (SQLException e) {System.out.println(e);}
        return customerPromoID;
    }

    //Check if a customer is eligible for a discount
    public static void checkRewardsEligible() {
        int customerID;
        String promoID;
        String inputPromoID;

        Scanner in = new Scanner(System.in);
        System.out.println("\nEnter PromoID:");
        inputPromoID = in.nextLine();

        System.out.println("\nEnter CustomerID:");
        customerID = in.nextInt();

        promoID = getRewardsEligible(customerID);

        if (promoID.equals(inputPromoID)){
            System.out.println("Customer is Eligible for PromoID");
        } else{
            System.out.println("CustomerID not eligible for the given PromoID");
        }
    }
    
    //Add a rows in Rewards_Eligible_For Table
    public static void addRewardsEligible(int customerID, String customerMembershiplevel){
        String promoID = "";
        try{
            prepGetPromoID.setString(1,customerMembershiplevel);
            ResultSet rs = prepGetPromoID.executeQuery();
            if (rs.next() == false) {
                System.out.println("Invalid CustomerID");
            } else {
                promoID = rs.getString("PromoID");
            }
        }catch (SQLException e) {System.out.println(e);}

        try {
            conn.setAutoCommit(false);
            try{
                prepAddRewardsEligible.setInt(1,customerID);
                prepAddRewardsEligible.setString(2,promoID);

                prepAddRewardsEligible.executeUpdate();
                conn.commit();
                System.out.println("Success");
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
    
    //Updates a row in Rewards_Eligibile_For Table
    public static void updateRewardsEligible(int customerID, String membershipLevel){
        String promoID = "";
        try{
            prepGetPromoID.setString(1,membershipLevel);
            ResultSet rs = prepGetPromoID.executeQuery();
            if (rs.next() == false) {
                System.out.println("Invalid CustomerID");
            } else {
                promoID = rs.getString("PromoID");
            }
        }catch (SQLException e) {System.out.println(e);}

        try {
            conn.setAutoCommit(false);
            try{
                prepUpdateRewardsEligible.setString(1,promoID);
                prepUpdateRewardsEligible.setInt(2,customerID);

                prepUpdateRewardsEligible.executeUpdate();
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

    //Adds a row in customerPaysBill 
    public static void addCustomerPaysBill(int customerID, int transactionID){
        try {
            conn.setAutoCommit(false);
            try{
                prepAddCustomerPaysBill.setInt(1,customerID);
                prepAddCustomerPaysBill.setInt(2,transactionID);

                prepAddCustomerPaysBill.executeUpdate();
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

    //Delete a row based on transactionId in customerPaysBill Table
    public static void deleteCustomerPaysBill(int transactionID){
        try {
            conn.setAutoCommit(false);
            try{
                prepDeleteCustomerPaysBill.setInt(1,transactionID);
                prepDeleteCustomerPaysBill.executeUpdate();
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

    //Return a Product list for a transaction made.
    public static ResultSet getProductList(int transactionID){
        ResultSet rs = null;
        try{
            prepGetProductList.setInt(1,transactionID);
            rs = prepGetProductList.executeQuery();
            return rs;
        }catch (SQLException e) {System.out.println(e);}
        return rs;
    }

    //Prints the Product list for a transaction made.
    public static void userGetProductList(){
        int transactionID;
        Scanner in = new Scanner(System.in);

        System.out.println("\nEnter transactionID:");
        transactionID = in.nextInt();
        ResultSet resultSet = getProductList(transactionID);
        try{
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (resultSet.next()) {
                for (int i = 2; i <= columnsNumber; i++) {
                    if (i > 2) System.out.print(",  ");
                    String columnValue = resultSet.getString(i);
                    System.out.print(rsmd.getColumnName(i)+" = "+columnValue);
                }
                System.out.println("");
            }
        }catch (SQLException e) {System.out.println(e);}
    }
    
    //return the quantity left for a product in the store.
    public static int getQuantityStoreStock(int storeID, String productID){
    
        try{
            prepGetQuantityStoreStock.setInt(1,storeID);
            prepGetQuantityStoreStock.setString(2,productID);

            ResultSet rs = prepGetQuantityStoreStock.executeQuery();

            if(rs.next()){
                return rs.getInt("Stock");
            }else{
                return -1;
            }
        }catch (SQLException e) {System.out.println(e);}
        return -1;
    }

    //Tranfer the stock between stores 
    public static void transferProduct(){
        int quantity;
        String productID;
        int yourStore;
        int sendStore;

        Scanner in = new Scanner(System.in);

        System.out.println("\nEnter your storeID:");
        yourStore = in.nextInt();

        System.out.println("\nEnter the storeID from where you want to get products:");
        sendStore = in.nextInt();

        System.out.println("\nEnter the ProductID which you want to request:");
        productID = in.next();

        System.out.println("\nEnter the quantity for the Product:");
        quantity = in.nextInt();

        //Check if sending store have the product and quantity.
        //getquantity of product.
        int avaliableQuantity = getQuantityStoreStock(sendStore, productID);

        if(avaliableQuantity >= quantity){
            //transferproduct
            updateStoreStock(sendStore, productID, avaliableQuantity - quantity);
            avaliableQuantity = getQuantityStoreStock(yourStore, productID);
            if(avaliableQuantity<0){
                addStoreStock(yourStore, productID, quantity);
            }else{
                updateStoreStock(yourStore, productID, (avaliableQuantity + quantity));
            }
            System.out.println("\nSuccess - Products are transfered between Store");
        }else{
            System.out.println("\nRequested Store has "+ avaliableQuantity +" quantity of product left in their stock");
        }
    }
    
    //Add a row in Merchandise Table
    public static void addMerchandise() {
        String productID;
        String productName;
        String supplierID;
        int quantity;
        String buyPrice;
        String marketPrice;
        String manufactureDate;
        String expirationDate;
        int isOnSale;

        Scanner in = new Scanner(System.in);

        System.out.println("\nEnter productID:");
        productID = in.nextLine();

        System.out.println("\nEnter productName:");
        productName = in.nextLine();

        System.out.println("\nEnter supplierID:");
        supplierID = in.nextLine();

        System.out.println("\nEnter buyPrice:");
        buyPrice = in.nextLine();

        System.out.println("\nEnter marketPrice:");
        marketPrice = in.nextLine();

        System.out.println("\nEnter manufactureDate(yyyy-mm-dd):");
        manufactureDate = in.nextLine();

        System.out.println("\nEnter expirationDate(yyyy-mm-dd):");
        expirationDate = in.nextLine();

        System.out.println("\nEnter quantity:");
        quantity = in.nextInt();

        System.out.println("\nEnter isOnSale: (for yes enter 1 and for no enter 0)");
        isOnSale = in.nextInt();        

        try {
            conn.setAutoCommit(false);
            try{
                prepAddMerchandise.setString(1,productID);
                prepAddMerchandise.setString(2,productName);
                prepAddMerchandise.setString(3,supplierID);
                prepAddMerchandise.setInt(4,quantity);
                prepAddMerchandise.setBigDecimal(5, new BigDecimal(buyPrice));
                prepAddMerchandise.setBigDecimal(6, new BigDecimal(marketPrice));
                prepAddMerchandise.setDate(7,java.sql.Date.valueOf(manufactureDate));
                prepAddMerchandise.setDate(8,java.sql.Date.valueOf(expirationDate));
                prepAddMerchandise.setInt(9,isOnSale);

                prepAddMerchandise.executeUpdate();
                conn.commit();
                System.out.println("Success");
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

    //Delete a row from Merchandise table
    public static void deleteMerchandise() {
        String productID;

        Scanner in = new Scanner(System.in);

        System.out.println("\nEnter your StoreID:");
        productID = in.nextLine();

		try {
			conn.setAutoCommit(false);
			try {
				prepDeleteMerchandise.setString(1, productID);
				prepDeleteMerchandise.executeUpdate();
				conn.commit();
                System.out.println("Success");
			} catch (SQLException e) {
				conn.rollback();
				e.printStackTrace();
			} finally {
				conn.setAutoCommit(true);
			}
		}
		 catch (SQLException e) {
			e.printStackTrace();
		}
	}

    //return a merchandise row for a given productID
    public static ResultSet getMerchandise(String productID){
        ResultSet rs = null;
        try{
            prepGetMerchandise.setString(1, productID);
            rs = prepGetMerchandise.executeQuery();
            if(rs.next()){
                return rs;
            }
            
        }catch (SQLException e) {System.out.println(e);}
        return rs;

    }

    //Updates the Merchandise Table
    public static void userUpdateMerchandise() {
        String productID = "";
        String productName = "";
        String supplierID = "";
        int quantity = 0;
        String buyPrice = "";
        String marketPrice = "";
        String manufactureDate = "";
        String expirationDate = "";
        int isOnSale = 0;

        Scanner in = new Scanner(System.in);

        System.out.println("\nEnter productID:");
        productID = in.next();
        ResultSet rs = getMerchandise(productID);
        try{
            productName = rs.getString("ProductName");
            supplierID = rs.getString("SupplierID");
            quantity = rs.getInt("Quantity");
            buyPrice = rs.getBigDecimal("BuyPrice").toString();
            marketPrice = rs.getBigDecimal("MarketPrice").toString();
            manufactureDate = rs.getDate("ManufactureDate").toString();
            expirationDate = rs.getDate("ExpirationDate").toString();
            isOnSale = rs.getInt("IsOnSale");
        }catch (SQLException e) {System.out.println(e);}
          

        int option = 0;
        while(option != 100) {
            System.out.println("1 - Update ProductName");
			System.out.println("2 - Update SupplierID");
			System.out.println("3 - Update Quantity");
            System.out.println("4 - Update BuyPrice");
			System.out.println("5 - Update MarketPrice");
			System.out.println("6 - Update ManufactureDate");
			System.out.println("7 - Update ExpirationDate");
			System.out.println("8 - Update IsOnSale");
            System.out.println("100 - Enter to Confirm changes");
            option = in.nextInt();
            switch(option){
                case 1: System.out.println("Enter the ProductName");
                        productName = in.next();
                        break;
                case 2: System.out.println("Enter the SupplierID");
                        supplierID = in.next();
                        break;
                case 3: System.out.println("Enter the Quantity");
                        quantity = in.nextInt();
                        break;
                case 4: System.out.println("Enter the BuyPrice");
                        buyPrice = in.next();
                        break;
                case 5: System.out.println("Enter the MarketPrice");
                        marketPrice = in.next();
                        break;
                case 6: System.out.println("Enter the ManufactureDate(yyyy-mm-dd)");
                        manufactureDate = in.next();
                        break;
                case 7: System.out.println("Enter the ExpirationDate(yyyy-mm-dd)");
                        expirationDate = in.next();
                        break;
                case 8: System.out.println("Enter the IsOnSale");
                        isOnSale = in.nextInt();
                        break;
                default:
                        break; 
            }
        }
        try {
			conn.setAutoCommit(false);
			try {
				prepUpdateMerchandise.setString(1, productName);
                prepUpdateMerchandise.setString(2, supplierID);
                prepUpdateMerchandise.setInt(3, quantity);
                prepUpdateMerchandise.setBigDecimal(4, new BigDecimal(buyPrice));
                prepUpdateMerchandise.setBigDecimal(5, new BigDecimal(marketPrice));
                prepUpdateMerchandise.setDate(6, java.sql.Date.valueOf(manufactureDate));
                prepUpdateMerchandise.setDate(7, java.sql.Date.valueOf(expirationDate));
                prepUpdateMerchandise.setInt(8, isOnSale);
                prepUpdateMerchandise.setString(9, productID);

				prepUpdateMerchandise.executeUpdate();
				conn.commit();
                System.out.println("Success");
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
    
    //Updates StoreStock Table
    public static void updateStoreStock(int storeId, String productId, int stock) {

        try {
            conn.setAutoCommit(false);
            try{
                prepUpdateStoreStock.setInt(1, stock);
                prepUpdateStoreStock.setInt(2, storeId);
                prepUpdateStoreStock.setString(3,productId);


                prepUpdateStoreStock.executeUpdate();
                conn.commit();
                System.out.println("\nSuccess - Products are updated in StoreStock");

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

    //Adds a new roe in StoreStock Table
    public static void addStoreStock(int storeId, String productId, int stock){
        // "INSERT INTO StoreStock (StoreID, ProductID, Stock) VALUES (?,?,?);";
        try {
            conn.setAutoCommit(false);
            try{
                prepAddStoreStock.setInt(1, storeId);
                prepAddStoreStock.setString(2, productId);
                prepAddStoreStock.setInt(3, stock);

                prepAddStoreStock.executeUpdate();
                conn.commit();
                System.out.println("\nSuccess - Products are added in StoreStock");

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

    //Returns a table for product and quantity present inside a store 
    public static ResultSet getStoreStock(int storeId) {
        ResultSet rs = null;
        // SELECT * FROM StoreStock WHERE StoreID = ?
        try {
            conn.setAutoCommit(false);
            try{
                prepGetStoreStock.setInt(1, storeId);

                rs = prepGetStoreStock.executeQuery();
                conn.commit();
                return rs;
            }catch (SQLException e) {
				conn.rollback();
				e.printStackTrace();
            } finally {
				conn.setAutoCommit(true);
			}
        }catch (SQLException e) {
			e.printStackTrace();
		}
        return rs;
    }

    //Deletes a row from a StoreStock Table
    public static void deleteStoreStock(int storeId, String productId) {
        // DELETE FROM StoreStock WHERE StoreID = ?;
        try {
            conn.setAutoCommit(false);
            try{
                prepDeleteStoreStock.setInt(1, storeId);
                prepDeleteStoreStock.setString(2, productId);
                prepDeleteStoreStock.executeUpdate();
                conn.commit();
                System.out.println("\nSuccess - Products are deleted from StoreStock");

            }catch (SQLException e) {
				conn.rollback();
				e.printStackTrace();
            } finally {
				conn.setAutoCommit(true);
			}
        }catch (SQLException e) {
			System.out.println(e);
			
		}
    }

    //Adds a row in StoreStock
    public static void userAddStoreStock(){
        String productID;
        int storeID;
        int quantity;

        Scanner in = new Scanner(System.in);
        
        System.out.println("\nEnter the StoreID:");
        storeID = in.nextInt();
        System.out.print("\nEnter the productID:");
        productID = in.next();
        System.out.println("\nEnter the quantity:");
        quantity = in.nextInt();

        addStoreStock(storeID, productID, quantity);
    }

    //Updates a row in Store Stock
    public static void userUpdateStoreStock(){
        String productID;
        int storeID;
        int quantity;

        Scanner in = new Scanner(System.in);
        System.out.println("\nEnter the StoreID:");
        storeID = in.nextInt();

        System.out.println("\nEnter the productID:");
        productID = in.next();
        
        System.out.println("\nEnter the quantity:");
        quantity = in.nextInt();

        updateStoreStock(storeID, productID, quantity);
    }

    //Delete a row from StoreStock
    public static void userDeleteStoreStock(){
        String productID;
        int storeID;

        Scanner in = new Scanner(System.in);
        System.out.println("\nEnter the StoreID:");
        storeID = in.nextInt();

        System.out.println("\nEnter the productID:");
        productID = in.next();

        deleteStoreStock(storeID, productID);
    }

    //Prints Quantity a product in the store
    public static void userGetQuantityStoreStock(){
        String productID;
        int storeID;
        int quantity;

        Scanner in = new Scanner(System.in);
        System.out.println("\nEnter the StoreID:");
        storeID = in.nextInt();

        System.out.println("\nEnter the productID:");
        productID = in.next();

        quantity = getQuantityStoreStock(storeID, productID);
        System.out.println("\nThe Store("+storeID+") has " + quantity + " amount of " +productID);

    }
   
    public static void addStaff(String StaffID, String StoreID, String Name, String Age, String Address, String JobTitle , String PhoneNumber, String Email, String JoiningDate) {
	//This function will Add a new Staff member
        try {
            conn.setAutoCommit(false);
            try{
                prepAddStaff.setInt(1,Integer.parseInt(StaffID));
                prepAddStaff.setInt(2,Integer.parseInt(StoreID));
                prepAddStaff.setString(3,Name);
                prepAddStaff.setInt(4, Integer.parseInt(Age));
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
    }

    public static void addTransaction(String TransactionID, String StoreID, String CustomerID, String CashierID, String PurchaseDate, BigDecimal TotalPrice) {
	//This will add a new Transaction table 
       try {
            conn.setAutoCommit(false);
            try{
                prepAddTransaction.setInt(1,Integer.parseInt(TransactionID));
                prepAddTransaction.setInt(2,Integer.parseInt(StoreID));
                prepAddTransaction.setInt(3,Integer.parseInt(CustomerID));
                prepAddTransaction.setInt(4, Integer.parseInt(CashierID));
                prepAddTransaction.setDate(5,java.sql.Date.valueOf(PurchaseDate));
                prepAddTransaction.setBigDecimal(6,TotalPrice);

                prepAddTransaction.executeUpdate();
                conn.commit();
            }catch (SQLException e) {
				System.out.println("Error, try Again!");	
				conn.rollback();
			//	e.printStackTrace();
            } finally {
				conn.setAutoCommit(true);
			}
        }catch (SQLException e) {
			System.out.println("Error, try Again!");
			//e.printStackTrace();
		}
    }
    public static void deleteTransaction(String TransactionID) {
    
				deleteCustomerPaysBill(Integer.parseInt(TransactionID));
			        deletePurchasedItems(TransactionID);
			 try {
                        	conn.setAutoCommit(false);	
				try{
				prepDeleteTransaction.setString(1, TransactionID);
				prepDeleteTransaction.executeUpdate();
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

    public static void updateTransaction() {
	//This Function will update a transaction. Note , a single Value can not be updated . 
	//Due to our Assumption: Need to replace the whole transaction entry with the new one. 
        String TransactionID;
        Scanner in = new Scanner(System.in);
        System.out.println("\nEnter Transaction ID to update:");	
        TransactionID = in.nextLine();
                       // conn.setAutoCommit(false);
			System.out.println("\nDeleting old transaction and dependencies:");
			deletePurchasedItems(TransactionID);
			deleteCustomerPaysBill(Integer.parseInt(TransactionID));
			deleteTransaction(TransactionID);			
			System.out.println("\nCreate new updated transaction with modifed values");
			userTransactionAdd();
			
        }
 
    public static void addPurchasedItems(String TransactionID, String ProductID, String Quantity) {
	//The Items which are presented in Transaction
        try {
            conn.setAutoCommit(false);
            try{

                prepAddPurchasedItems.setInt(1,Integer.parseInt(TransactionID));
                prepAddPurchasedItems.setString(2,ProductID);
                prepAddPurchasedItems.setInt(3,Integer.parseInt(Quantity));

                prepAddPurchasedItems.executeUpdate();
                conn.commit();
            }catch (SQLException e) {
				System.out.println("Error, try Again!");
				conn.rollback();
				//e.printStackTrace();
            } finally {
				conn.setAutoCommit(true);
			}
        }catch (SQLException e) {
			//e.printStackTrace();
		}
    }

   

   public static void deletePurchasedItems(String TransactionID) {
     //Delete Transaction related entry from the PurchasedItems table   
                try {
                        conn.setAutoCommit(false);
                        try {
                                prepDeletePurchasedItems.setString(1, TransactionID);
                                prepDeletePurchasedItems.executeUpdate();
                                conn.commit();
                        } catch (SQLException e) {
                                conn.rollback();
                                e.printStackTrace();
                        } finally {
                                conn.setAutoCommit(true);
                        }
                } catch (SQLException e) {
			System.out.println(e);
                }
		
        //	}
                
	}

    public static BigDecimal getPrice(String ProductID, String CustomerID, String PurchaseDate) {
	//Calculate Discounted Price for a given Product and Customer.
    	BigDecimal price = null;
        int isonsale = 0;
        String PromoID = getRewardsEligible(Integer.parseInt(CustomerID));
        BigDecimal discount = null;
        String validthrough = null;
        try {
            

                prepGetPrice.setString(1,ProductID);
                ResultSet rs = prepGetPrice.executeQuery();
                if (rs.next()) {
                        price = rs.getBigDecimal("MarketPrice");
			isonsale= rs.getInt("IsOnSale");
			}
		if(PromoID!=""){
		prepGetDiscount.setString(1,PromoID);
		ResultSet rs_2 = prepGetDiscount.executeQuery();
		if(rs_2.next()){
			discount = rs_2.getBigDecimal("Discount");
			validthrough = rs_2.getDate("ValidThrough").toString();
		
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try{
			Date d1 = sdf.parse(validthrough);
			Date d2 = sdf.parse(PurchaseDate);
		
		if(isonsale==1 && d1.compareTo(d2)>0){
			price = price.subtract(price.multiply(discount));}
		}catch (ParseException e) {
                        e.printStackTrace();
                }
		}
        }catch (SQLException e) {
		System.out.println("Error, In Get Price!");
			e.printStackTrace();
		}
        return price;
    }

	public static boolean isActiveClub() {
		//Check if customer is Active
		Scanner in = new Scanner(System.in);
		System.out.println("Enter Customer ID");		
		String CustomerID= in.nextLine();

		String isactive = null;
		try {
		prepGetCustomer.setInt(1,Integer.parseInt(CustomerID));
                ResultSet rs =prepGetCustomer.executeQuery();
		
                if (rs.next()) {
                        isactive= rs.getString("ActiveStatus");
                        }else{
			System.out.println("Wrong Cust ID:");
			return false;
		}
		if(isactive.equals("Active")){
			System.out.println("\n\n------------------This Customer is Active!---------------------\n\n");
			return true;
		}

		}catch (SQLException e) {
                        e.printStackTrace();
                }
		System.out.println("\n\n------------------This Customer is In-Active!---------------------\n\n");
		return false;	

	}

       public static void generateSupplierBill(){
	//generate Bills to Suplier
	//Input: Shipment ID
	//It will then count the total price for that Shipment delivery
	//The price will be calculated based on BuyPrice
	//This Bill is then added in the ShipmentBill table
		System.out.println("Choose shipment from below list to generate Bills for: \n");			
		String ShipmentID=null;
	    	String SentBy = null;
   		String ReceivedBy = null;
  	  	String SentDate = null;
  	  	String ReceivedDate = null;
		String date="2021-04-20";
  
	try{
 	 try{
        	//prepGetShipment.setString(1,ShipmentID);
        	ResultSet rs = prepGetSupShipment.executeQuery();
		while (rs.next()) {
		ShipmentID = rs.getString("ShipmentID");	
       		SentBy = rs.getString("SentBy");
        	ReceivedBy = rs.getString("ReceivedBy");
        	SentDate = rs.getString("SentDate");
        	ReceivedDate = rs.getString("ReceivedDate");

               	 	System.out.print(ShipmentID + " | ");
                	System.out.print(SentBy + " | ");
                	System.out.print(ReceivedBy + " | ");
                	System.out.print(SentDate + " | ");
			System.out.print(ReceivedDate + "\n");
            	}

   	 } catch (SQLException e) {
        e.printStackTrace();
   	 }

		Scanner in = new Scanner(System.in);
                System.out.println("Enter Supplier Shipment ID");
                ShipmentID = in.nextLine();
		System.out.println("Enter Your BillingExecutive ID");
		int BillingExecutiveID = in.nextInt();
		String prod =null;
		int Qnty= 0;
		BigDecimal price = BigDecimal.ZERO;
	 	BigDecimal Amount = BigDecimal.ZERO;	
	try{ 
		prepGetShipmentConsistsOf.setString(1, ShipmentID);
		ResultSet rs2 = prepGetShipmentConsistsOf.executeQuery();
		while(rs2.next()){
		
			prod = rs2.getString("ProductID");
			Qnty = rs2.getInt("Quantity");

			try{
				prepGetMerchandise.setString(1,prod);
                		ResultSet rs3= prepGetMerchandise.executeQuery();
				if (rs3.next()) {
                        		price = rs3.getBigDecimal("BuyPrice");
                        		//	isonsale= rs.getInt("IsOnSale");
                        	}
				

			}catch (SQLException e) {
       				 e.printStackTrace(); }

                        BigDecimal price_temp = price.multiply(new BigDecimal(Qnty));
			Amount = Amount.add(price_temp);
		}	

	}catch (SQLException e) {
				System.out.println("Error, try Again!");
                                // e.printStackTrace(); 
                               }

	try{

		conn.setAutoCommit(false);
		
		prepAddShipmentBill.setString(1,ShipmentID);
		prepAddShipmentBill.setBigDecimal(2,Amount);
		prepAddShipmentBill.setDate(3,java.sql.Date.valueOf(date));
		prepAddShipmentBill.setInt(4,BillingExecutiveID);	
		
		prepAddShipmentBill.executeUpdate();
	



		} catch (SQLException e) {
			        System.out.println("Sorry Could not generate Bill!");
                                conn.rollback();
                                e.printStackTrace();
                        } finally {
				
                                conn.setAutoCommit(true);
                        }



	}catch (SQLException e) {
			System.out.println("Error, try Again!");
                                // e.printStackTrace();
                           }

	}


	
	public static void userTransactionAdd() {
	//Adding new Transaction
	//Also updates its child tables, such as Purchased Items & CustomerPaysBillTo
	//It makes call to Get Price and then calculate Total price
	// Local Variable
	String transactionid;
        String storeid ;
        String customerid ;
        String cashierid;
        String purchasedate ;
        String productlist; 
        String productid;
        String quantity;
        BigDecimal price; 
        BigDecimal totalprice = BigDecimal.ZERO; 
	Scanner in = new Scanner(System.in);


		try { conn.setAutoCommit(false);
			try{
			// Get staff id for the new staff
			System.out.println("\nEnter the transaction ID :");
			transactionid = in.nextLine();
			// Get StoreID
			System.out.println("\nEnter the store ID :\n");
			storeid = in.nextLine();
            		// Get name
			System.out.println("\nEnter the customer ID :\n");
			customerid = in.nextLine();
			// Get age
			System.out.println("\nEnter the cashier ID :\n");
			cashierid = in.nextLine();
			// Get address
			System.out.println("\nEnter the purchase date :\n");
			purchasedate = in.nextLine();
			// Get job title
			System.out.println("\nEnter the purchased product ID list with quantity [i.e 3001:3,3003:3] :\n");
			productlist = in.nextLine();
            		String[] res = productlist.split(",");
            		for(String myStr: res) {
                    		String[] array = myStr.split(":");
                    		productid = array[0];
                    		quantity = array[1];
                    		price= getPrice(productid,customerid,purchasedate);
			        int avaliableQuantity = getQuantityStoreStock(Integer.parseInt(storeid), productid);

				if(avaliableQuantity >= Integer.parseInt(quantity)){
				
				}else{
           			 System.out.println("\nRequested Store Do Not Have Enough Quantity, Transaction Failed!");
				 return;
       				 }

		    		BigDecimal price_temp = price.multiply(new BigDecimal(quantity));	
                    		totalprice = totalprice.add(price_temp);
           		 }
            

			// call function that interacts with the Database
			addTransaction(transactionid,storeid,customerid, cashierid, purchasedate, totalprice);
			
			for(String myStr: res) {
                    		String[] array = myStr.split(":");
                    		productid = array[0];
                    		quantity = array[1];
				addPurchasedItems(transactionid, productid, quantity);
                                int avaliableQuantity = getQuantityStoreStock(Integer.parseInt(storeid), productid);

                                if(avaliableQuantity >=Integer.parseInt(quantity)){
					updateStoreStock(Integer.parseInt(storeid), productid, avaliableQuantity - Integer.parseInt(quantity));
                                }else{
				
                                 System.out.println("\nRequested Store Do Not Have Enough Quantity!");
                                 }

			}
			addCustomerPaysBill(Integer.parseInt(customerid),Integer.parseInt(transactionid));
			conn.commit();
			System.out.println("A new Transaction is added successfully!");
		}catch (Throwable err) {
				System.out.println("Error, try Again!");
				//System.out.println(err);
				conn.rollback();
			} finally {
				conn.setAutoCommit(true);
			}
		} catch (Throwable err) {
			 System.out.println("Error, try Again!");
			//System.out.println(err);
	
		}
	}
	public static void userTransactionDelete(){
		String TransactionID;
		Scanner in = new Scanner(System.in);
		System.out.println("\nEnter Transaction ID to delete");
		TransactionID = in.nextLine();
		deleteTransaction(TransactionID);
		System.out.println("\nTransaction has been deleted!");	

	}
	public static void userStaffAdd() {
	//User Interactive API for Adding StaffID
	//Child TAbles such as Admin,Cashier etc will be modified as per the input provided
		// Declare local variables
	String staffID;
        String storeID ;
        String name ;
        String age;
        String address ;
        String jobtitle ;   
        String phonenumber ;
        String email ;  
        String joiningdate ;
	Scanner in = new Scanner(System.in);


		try {
			// Get staff id for the new staff
			System.out.println("\nEnter the staff ID of the new staff:");
			staffID = in.nextLine();
			// Get StoreID
			System.out.println("\nEnter the store ID of the new staff:\n");
			storeID = in.nextLine();
            		// Get name
			System.out.println("\nEnter the name of the new staff:\n");
			name = in.nextLine();
			// Get age
			System.out.println("\nEnter the age of the new staff:\n");
			age = in.nextLine();
			// Get address
			System.out.println("\nEnter the address of the new staff:\n");
			address = in.nextLine();
			// Get job title
			System.out.println("\nEnter the job title of the new staff:\n");
			jobtitle = in.nextLine();
			// Get phone
			System.out.println("\nEnter the phone of the new staff:\n");
			phonenumber = in.nextLine();
            		// Get email
			System.out.println("\nEnter the email of the new staff:\n");
			email = in.nextLine();

            		// Get joining date
			System.out.println("\nEnter the joining date of the new staff:\n");
			joiningdate = in.nextLine();

			// call function that interacts with the Database
			addStaff(staffID,storeID,name,age,address,jobtitle,phonenumber,email,joiningdate);
			addPositionTables(Integer.parseInt(staffID),jobtitle);
			System.out.println("A new staff is added successfully!");
		} catch (Throwable err) {
			System.out.println(err);
			
		}
	}

    public static void deleteStaff() {
		System.out.println("You can not delete a Staff Member");
		/*Scanner in = new Scanner(System.in);		
		System.out.println("\nEnter the staff id of the staff you want to delete:\n");
		String StaffID = in.nextLine();
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
			System.out.println("Error, try Again!");
			//System.out.println(e);
		}*/
	}

   public static void updateStaff() {
	// Update Specific information about the Saff Member
	// Wont be able to Update ID
	Scanner in = new Scanner(System.in);
        System.out.println("\nEnter the staff id of the staff you want to update:\n");
        try{
	String StaffID = in.nextLine();

        
	prepGetStaff.setInt(1, Integer.parseInt(StaffID));
	ResultSet rs = prepGetStaff.executeQuery();

	Integer StoreID =0;
        String Name =null;
        Integer Age = 0;
        String Address =null;
        String JobTitle =null;
        String newJobTitle = null;
        String PhoneNumber =null;
        String Email =null;
        String JoiningDate = null ;

	while(rs.next()){
            StoreID = rs.getInt("StoreID");
            Name = rs.getString("Name");
            Age = rs.getInt("Age");
            Address = rs.getString("Address");
            JobTitle = rs.getString("JobTitle");   
	    newJobTitle = JobTitle;
            PhoneNumber = rs.getString("PhoneNumber");
            Email = rs.getString("Email");  
            JoiningDate = rs.getDate("JoiningDate").toString();

     	    System.out.println("\nStaff ID: " + StaffID + ", storeID: " + StoreID + ", age: " + Age + ", name: " +Name 
					+ ", job title: " + JobTitle +  ", Address: " + Address
					+ ", phone: " + PhoneNumber + ", email: " + Email);
     	}
        int option = 0;
        while(option != 100) {
            System.out.println("\n1 - Update StoreID");
			System.out.println("\n2 - Update Name");
			System.out.println("\n3 - Update Age");
            System.out.println("\n4 - Update Address");
			System.out.println("\n5 - Update Job Title");
            System.out.println("\n6 - Update Phone number");
			System.out.println("\n7 - Update Email ID ");
			System.out.println("\n8 - Update Joining Date");



            System.out.println("\n100 - Confirm\n");
            option = Integer.parseInt(in.nextLine());
            switch(option){
                case 1: System.out.println("\nEnter the StoreID");
                        StoreID = Integer.parseInt(in.nextLine());
                        break;
                case 2: System.out.println("\nEnter the Name");
                        Name = in.nextLine();
                        break;
                case 3: System.out.println("\nEnter the Age");
                        Age = Integer.parseInt(in.nextLine());
                        break;
                case 4: System.out.println("\nEnter the Address");
                        Address = in.nextLine();
                        break;
                case 5: System.out.println("\nEnter the job title");
                        newJobTitle = in.nextLine();
                        updatePositionTables(Integer.parseInt(StaffID), JobTitle, newJobTitle);
                        break;
                case 6: System.out.println("\nEnter the phone number");
                        PhoneNumber = in.nextLine();
                        break;
                case 7: System.out.println("\nEnter the email address");
                        Email = in.nextLine();
                        break;
                case 8: System.out.println("\nEnter the joining date (yyyy-mm-dd)");
                        JoiningDate = in.nextLine();
                        break;
                default:
                        break; 
            }
        }
        try {
			conn.setAutoCommit(false);
			try {
                
                prepUpdateStaff.setInt(1,StoreID);
                prepUpdateStaff.setString(2,Name);
                prepUpdateStaff.setInt(3, Age);
                prepUpdateStaff.setString(4,Address);
                prepUpdateStaff.setString(5,newJobTitle);
                prepUpdateStaff.setString(6,PhoneNumber);
                prepUpdateStaff.setString(7, Email);
                prepUpdateStaff.setDate(8,java.sql.Date.valueOf(JoiningDate));
                prepUpdateStaff.setInt(9,Integer.parseInt(StaffID));
				prepUpdateStaff.executeUpdate();
				conn.commit();
			} catch (SQLException e) {
				conn.rollback();
				System.out.println(e);

			} finally {
				conn.setAutoCommit(true);
			}
		} catch (SQLException e) {
			System.out.println(e);
			
		}
	} catch (SQLException e) {
			System.out.println(e);
                        
                }

    }

   public static void addPositionTables(int staffId, String newPosition){
	//Adding Staff to its respective Postion tables, such as Admin,Cashier etc
	try{
	switch (newPosition) {
                case "BillingExecutive":
                    prepAddBillingExecutive.setInt(1, staffId);
                    prepAddBillingExecutive.executeUpdate();
                break;
                case "WarehouseOperator":
                    prepAddWarehouseOperator.setInt(2, staffId);
                    prepAddWarehouseOperator.executeUpdate();
                break;
                case "Cashier":
                    prepAddCashier.setInt(1, staffId);
                    prepAddCashier.executeUpdate();
                break;
                case "RegistrationStaff":
                    prepAddRegistrationStaff.setInt(1, staffId);
                    prepAddRegistrationStaff.executeUpdate();
                break;
                case "Admin":
                    prepAddAdmin.setInt(1, staffId);
                    prepAddAdmin.executeUpdate();
                break;
                default:
                break;
            }
            conn.commit();
            conn.setAutoCommit(true);

        } catch (Exception e) {
            try {conn.rollback();} catch(Exception z) {System.out.println(z);}
            System.out.println(e);
        }


   }




    
    public static void updatePositionTables(int staffId, String currentPosition, String newPosition){

	// If a Staff position is changed by admin, changes will be reflected in DB
        try {
            conn.setAutoCommit(false);
            switch (currentPosition) {
                case "BillingExecutive":
                    prepDeleteBillingExecutive.setInt(1, staffId);
                    prepDeleteBillingExecutive.executeUpdate();
                break;
                case "WarehouseOperator":
                    prepDeleteWarehouseOperator.setInt(1, staffId);
                    prepDeleteWarehouseOperator.executeUpdate();
                break;
                case "Cashier":
                    prepDeleteCashier.setInt(1, staffId);
                    prepDeleteCashier.executeUpdate();
                break;
                case "RegistrationStaff":
                    prepDeleteRegistrationStaff.setInt(1, staffId);
                    prepDeleteRegistrationStaff.executeUpdate();
                break;
                case "Admin":
                    prepDeleteAdmin.setInt(1, staffId);
                    prepDeleteAdmin.executeUpdate();
                break;
                default:
                break;
            }
            switch (newPosition) {
                case "BillingExecutive":
                    prepAddBillingExecutive.setInt(1, staffId);
                    prepAddBillingExecutive.executeUpdate();
                break;
                case "WarehouseOperator":
                    prepAddWarehouseOperator.setInt(2, staffId);
                    prepAddWarehouseOperator.executeUpdate();
                break;
                case "Cashier":
                    prepAddCashier.setInt(1, staffId);
                    prepAddCashier.executeUpdate();
                break;
                case "RegistrationStaff":
                    prepAddRegistrationStaff.setInt(1, staffId);
                    prepAddRegistrationStaff.executeUpdate();
                break;
                case "Admin":
                    prepAddAdmin.setInt(1, staffId);
                    prepAddAdmin.executeUpdate();
                break;
                default:
                break;
            }
            conn.commit();
            conn.setAutoCommit(true);

        } catch (Exception e) {
            try {conn.rollback();} catch(Exception z) {System.out.println(z);}
            System.out.println(e);
        }
    }

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
            System.out.println("StoreID | Manager | Address | Phone numbers");
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
                in.nextLine();

                if (updatedAttribute == 1) {
                    System.out.println("Enter new address:");
                    String address = in.nextLine();
                    prepUpdateStoreAddress.setString(1, address);
                    prepUpdateStoreAddress.setInt(2, storeId);
                    prepUpdateStoreAddress.executeUpdate();
                    validInput = true;
                    System.out.println("Address updated successfully");
                } else if (updatedAttribute ==2){

                    System.out.println("Enter new phone number");
                    String phone = in.nextLine();
                    //prepUpdateStorePhone.setString(1, "PhoneNumber");
                    prepUpdateStorePhone.setString(1, phone);
                    prepUpdateStorePhone.setInt(2, storeId);
                    prepUpdateStorePhone.executeUpdate();
                    validInput = true;
                    System.out.println("Phone number updated successfully");
                }
                else {
                    System.out.println("Not a valid option, try again");
                }
            }
        } catch (Exception e) {
            System.out.println(prepUpdateStore);
            System.out.println(e);
        }


        // Execute update
    }

    public static void addReport(String ReportType, String ReportDetail){
	// Adding a new Report
	 try {
            conn.setAutoCommit(false);
            try{
                prepAddReport.setString(1,ReportType);
                prepAddReport.setString(2,ReportDetail);

                prepAddReport.executeUpdate();
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



    public static void customerGrowthReport(){
        int newSignups;
        int totalSignups;
        String date = "";

        in = new Scanner(System.in);

        System.out.println("Customer growth report: Enter the desired month and year (format YYYY/MM)");
        while (!in.hasNext(monthYear)){
            System.out.println("Invalid date format, try again");
            in.nextLine();
        };
        date = in.nextLine();
        date = date + "/01"; // put date in format YYYY/MM/DD

        try {

            prepCustomerReport.setString(1, date);
            prepCustomerReport.setString(2, date);

            ResultSet rs = prepCustomerReport.executeQuery();
            // SELECT SUM(IF(SignupDate >= DATEADD(month,-1,GETDATE()), 1, 0)) AS new_signups
            // COUNT(*) AS total_signups

            if(rs.next()){
                newSignups = rs.getInt("new_signups");
                totalSignups = rs.getInt("total_signups");

		
                System.out.println("There was " + newSignups + " signups in that month (" + totalSignups + " signups total)");
            }

        } catch (SQLException e) {
			e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        
        // Setup db connection w username and password
        System.out.println("Connecting to database...\n");
        conn = getConnection();
        // TO DO if connection can't be made then try again or exit

        generatePreparedStatement();
	in = new Scanner(System.in);
        // Create db and tables- loop through commands and execute each
	System.out.println("Do you want to Load Fresh Data? 1-Yes, 2- No\n");
	int option = in.nextInt();
	if(option==1){
        	System.out.println("Loading data...\n");
        	setupDb();
	}

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
                                showOptions(2);
                                menu =2;
                                break;
                            case 3:
                                updateMember();
                                showOptions(2);
                                menu =2;
                                break;
                    
			                case 4:
			                    userTransactionAdd();                                
                                showOptions(2);
                                menu =2;
			                    break;
			                case 5:
                                userTransactionDelete();
                                showOptions(2);
                                menu =2;
			                    break;
			                case 6:
                                updateTransaction();
                                showOptions(2);
                                menu =2;
			                break;
                        // To do: Build out remaining options
		
			    case 7:
                            	isActiveClub();
				//updateTransaction();
                                showOptions(2);
                                menu =2;
                            break;
						    
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

                        case 2:
                            customerGrowthReport();
                            showOptions(3);
                            menu = 3;
                        break;
			case 3:
			    generateSupplierBill();
                            showOptions(3);
                            menu = 3;	
			break;
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
                        case 2:
                            userAddStoreStock();
                            showOptions(4);
                            menu = 4;
                            break;
                        case 3:
                            userDeleteStoreStock();
                            showOptions(4);
                            menu = 4;
                            break;
                        case 4:
                            userUpdateStoreStock();
                            showOptions(4);
                            menu = 4;
                            break;
                        case 5:
			                transferProduct();
                            showOptions(4);
                            menu = 4;
                            break;
                        case 6:
                            userGetQuantityStoreStock();
                            showOptions(4);
                            menu = 4;
                            break;
                        case 7:
                            addMerchandise();
                            showOptions(4);
                            menu = 4;
                            break;
                        case 8:
                            deleteMerchandise();
                            showOptions(4);
                            menu = 4;
                            break;
                        case 9:
                            userUpdateMerchandise();
                            showOptions(4);
                            menu = 4;
                            break;
                        case 10:
                            enterShipmentinfo();
                            showOptions(4);
                            menu = 4;
                            break;
                        case 11:
                            deleteShipmentInfo();
                            showOptions(4);
                            menu = 4;
                            break;
                        case 12:
                            updateShipmentinfo();
                            showOptions(4);
                            menu = 4;
                            break;

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
                            showOptions(5);
                            menu = 5;
                        break;

                        // Delete a store
                        case 3:
                            deleteStore();
                            showOptions(5);
                            menu = 5;
                            break;

                        case 4:
                            updateStore();
                            showOptions(5);
                            menu = 5;
                        break;

                        case 5:
                            addDiscount();
                            showOptions(5);
                            menu = 5;
                            break;
                        case 6:
                            deleteDiscount();
                            showOptions(5);
                            menu = 5;
                            break;
                        case 7:
                            updateDiscount();
                            showOptions(5);
                            menu = 5;
                            break;
                        case 8:
                            addMerchandise();
                            showOptions(5);
                            menu = 5;
                            break;
                        case 9:
                            deleteMerchandise();
                            showOptions(5);
                            menu = 5;
                            break;
                        case 10:
                            userUpdateMerchandise();
                            showOptions(5);
                            menu = 5;
                            break;
                        case 11:
                            checkRewardsEligible();
                            showOptions(5);
                            menu = 5;
                            break;
                        case 12:
                            userGetProductList();
                            showOptions(5);
                            menu = 5;
                            break;
                        case 13:
                       	    userStaffAdd();
                               showOptions(5);
                               menu = 5;
                            break;
			            case 14:
                             updateStaff();
                             showOptions(5);
                             menu = 5;
                             break;
			            case 15:
                             deleteStaff();
                             showOptions(5);
                             menu = 5;
                             break;
                        case 16:
                            enterSupplierinfo();
                            showOptions(5);
                            menu = 5;
                            break;
                        case 17:
                            deleteSupplierInfo();
                            showOptions(5);
                            menu = 5;
                            break;
                        case 18:
                            updateSupplierinfo();
                            showOptions(5);
                            menu = 5;
                            break;
                        case 19:
                            enterShipmentinfo();
                            showOptions(5);
                            menu = 5;
                            break;
                        case 20:
                            deleteShipmentInfo();
                            showOptions(5);
                            menu = 5;
                            break;
                        case 21:
                            updateShipmentinfo();
                            showOptions(5);
                            menu = 5;
                            break;
                        case 22:
                            generateShopSalesGrowthReport();
                            showOptions(5);
                            menu = 5;
                            break;
                        case 23:
                            generateCustomerActivityReport();
                            showOptions(5);
                            menu = 5;
                            break;
                        case 24:
                            generateTotalSalesReport();
                            showOptions(5);
                            menu = 5;
                            break;
                        case 25:
                            generateStoreStockReport();
                            showOptions(5);
                            menu = 5;
                            break;
                        case 26:
                            generateProductStoreStockReport();
                            showOptions(5);
                            menu = 5;
                            break;
                        case 27:
                            generatePlatinumCashback();
                            showOptions(5);
                            menu = 5;
                            break;
			case 28:
			    handleReturns();
			    showOptions(5);
			    menu = 5;
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
            System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------\n");
	    System.out.println("\tWelcome to WolfWR Management System: Wholesale Store chain!");
	    System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------\n"); 
            System.out.println("\nWhich department do you belong to? Enter the corresponding number:");
            System.out.println("2 - Registration Staff\n3 - Billing Staff\n4 - Warehouse\n5 - Admin\nEnter 0 to quit");
            break;

            // registration staff options
            //
            case 2:
            System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------\n");
            System.out.println("\tWelcome registration staff. Please choose from the available options below:");
            System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------\n");
            System.out.println("\t0 - Exit program\n\t1 - Return to main menu");
            System.out.println("\t2 - Signup a new club member");
            System.out.println("\t3 - Update an existing member's information");
            System.out.println("\t4 - Add new transaction information");
            System.out.println("\t5 - Delete existing Transaction");
            System.out.println("\t6 - Update existing Transaction");	
            System.out.println("\t7 - Check Active Status");
            break;

            // billing staff options
            case 3:
            System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------\n");
            System.out.println("\tWelcome billing staff. Please choose from the available options below:");
            System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------\n");
            System.out.println("\t0 - Exit program\n\t1 - Return to main menu");
            System.out.println("\t2 - View the customer growth report");           
	        System.out.println("\t3 - Generate Bill for the Supplier");
            break;

            // warehouse operator options
            case 4:
            System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------\n");
            System.out.println("\tWelcome warehouse staff. Please choose from the available options below:");
            System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------\n");
            System.out.println("\t0 - Exit program\n\t1 - Return to main menu");
            System.out.println("\t2 - Add storeStock");
            System.out.println("\t3 - Delete storeStock");
            System.out.println("\t4 - Update storeStock");
            System.out.println("\t5 - Transfer Stock from store to store");
            System.out.println("\t6 - Get Quantity of product in storeStock");
            System.out.println("\t7 - Add Merchandise");
            System.out.println("\t8 - Delete Merchandise");
            System.out.println("\t9 - Update Merchandise");
            System.out.println("\t10 - Add Shipment");
            System.out.println("\t11 - Delete Shipment");
            System.out.println("\t12 - Update Shipment");

            break;

            // admin options
            case 5:
            System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------\n");
            System.out.println("\tWelcome admin. Please choose from the available options below:");
            System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------\n");
            System.out.println("\t0 - Exit program\n\t1 - Return to main menu");
            System.out.println("\n\t***************     Store     ********************\n");
            System.out.println("\t2 - Add a new store");
            System.out.println("\t3 - Delete a store");
            System.out.println("\t4 - Update a store's information");
            System.out.println("\n\t***************   Discount    ********************\n");
            System.out.println("\t5 - Add Discount");
            System.out.println("\t6 - Delete Discount");
            System.out.println("\t7 - Update Discount");
            System.out.println("\n\t***************   Inventory   ********************\n");
            System.out.println("\t8 - Add Merchandise");
            System.out.println("\t9 - Delete Merchandise");
            System.out.println("\t10 - Update Merchandise");
            System.out.println("\t11 - Check if Customer is Eligible for Rewards");
            System.out.println("\t12 - Get list of product for transaction ID");
            System.out.println("\n\t***************   StaffMember ********************\n");
            System.out.println("\t13 - Add a Staff Member");
            System.out.println("\t14 - Update a Staff Information");
            System.out.println("\t15 - Delete a Staff Member");
            System.out.println("\n\t***************   Supplier    ********************\n");
            System.out.println("\t16 - Enter new Supplier");
            System.out.println("\t17 - Delete a Supplier");
            System.out.println("\t18 - Update information of a supplier");
            System.out.println("\n\t***************   Shipment    ********************\n");
            System.out.println("\t19 - Enter new Shipment");
            System.out.println("\t20 - Delete a shipment");
            System.out.println("\t21 - Update information of a shipment");
	    System.out.println("\n\t***************    Report     ********************\n");
            System.out.println("\t22 - Store sales report");
            System.out.println("\t23 - Customer activity report");
            System.out.println("\t24 - Total Sales Report By Month,Year,Day");
            System.out.println("\t25 - Store Stock Report for a Store");
            System.out.println("\t26 - Store Stock Report for a ProductID");
            System.out.println("\t27 - Get 2% cashback amount for a customer");
            System.out.println("\t28 - Handle Returns");



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
        String membershipLevel;
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

        System.out.println("MembershipLevel(Standard/Gold/Platinum):");
        membershipLevel = in.nextLine();

        query = String.format("INSERT INTO ClubMember (ActiveStatus,Name,MembershipLevel,Address,Phone,Email) VALUES (\"Active\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\")", name, membershipLevel, address, phone, email);

        try (Statement stmt = con.createStatement()){
            stmt.executeQuery(query);
            //Adding Entry in Rewards_Eligible_for
            try{
                ResultSet rs = prepGetCustomerID.executeQuery();
                if(rs.next()){
                    customerId = rs.getInt("CustomerID");
                    System.out.println("CustomerID = " + customerId);
                    membershipLevel = rs.getString("MembershipLevel");
                    addRewardsEligible(customerId, membershipLevel);
                }
            }catch (SQLException e) {System.out.println(e);} 
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
        String membershipLevel = "";
        int updatedAttribute = -1;

        System.out.println("Enter the customerID of the user to be updated");
        Scanner in = new Scanner(System.in);
        customerId = in.nextInt();

        // Get the customerid for user to be updated
        try (Statement stmt = conn.createStatement()){
            prepGetCustomer.setInt(1, customerId);
            ResultSet rs = prepGetCustomer.executeQuery();
            while (rs.next()) {
                // Store all the current values
                // Might be overriden based on user input
                name = rs.getString("Name");
                activeStatus = rs.getString("ActiveStatus");
                name = rs.getString("Name");
                address = rs.getString("Address");
                phone = rs.getString("Phone");
                email = rs.getString("Email");
                membershipLevel = rs.getString("MembershipLevel");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        System.out.println("Choose which data to update:");
        System.out.println("1 - Active status\n2 - Name\n3 - Address\n4 - Phone number\n5 - Email address \n6 - MembershipLevel");
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
            case 6:
                System.out.println("Enter users updated Membership");
                in.nextLine();
                membershipLevel = in.nextLine();
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
                prepUpdateCustomer.setString(6, membershipLevel);

                prepUpdateCustomer.setInt(7, customerId);

                prepUpdateCustomer.executeUpdate();
                conn.commit();
                if(activeStatus.equals("Active")){
                    updateRewardsEligible(customerId, membershipLevel);
                }
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
