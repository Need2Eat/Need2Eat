package at.need2eat.need2eat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.Image;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;


import at.need2eat.need2eat.Product;

/**
 * Created by Tomi on 25.11.2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper implements DatabaseManager{
    static Connection c = null;

  /**
   * This "openConnection"-Method creates a connection
   * to our internal database
   */
    public static void openConnection(){
      try {
        //Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:Product");
      } catch ( Exception e ) {
        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        System.exit(0);
      }
      System.out.println("Opened database successfully");
    }
  private static final int DATABASE_VERSION = 1;

  // Database Name
  private static final String DATABASE_NAME = "Product";

  // Products table name
  private static final String TABLE_PRODUCTS = "Product";

  private static Image image;
  // Products Table Columns gtin, id, expiryDate
  private static final String KEY_GTIN = "gtin";
  public static int KEY_ID;
  public static String id = "id";
  public static String KEY_NAME = "name";
  public static Date KEY_EXPIRYDATE;

  public DatabaseHandler(Context context) {

    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  public static SQLiteDatabase db;

  // Creating Tables
  public void onCreate(SQLiteDatabase db) {
    String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
        + KEY_GTIN+ " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"+
        KEY_EXPIRYDATE+"TEXT"+
        id+ " INT auto_increment"+")";
    db.execSQL(CREATE_PRODUCTS_TABLE);
  }

  // Upgrading database
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Drop older table if existed
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);

    // Create tables again
    onCreate(db);
  }

  @Override
  public Product getProduct(int id) {
    return new Product(id,KEY_GTIN,KEY_NAME, KEY_EXPIRYDATE);
  }

  @Override
  public List<Product> getAllProducts() {
    List<Product> list = new ArrayList<Product>();
    list.add(new Product(KEY_GTIN,KEY_NAME,KEY_EXPIRYDATE));
    return list;
  }

  // Adding new product
  public void addProduct(Product product) {
    db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(KEY_NAME, product.getName());

    // Inserting Row
    db.insert(TABLE_PRODUCTS, null, values);
    db.close();
  }

  @Override
  public void updateProduct(Product newProduct) {

  }

  @Override
  public void deleteProduct(int id) {
    Cursor c = db.rawQuery("DELETE * FROM Products WHERE id="+id, null);
    if(c.moveToFirst()){
      do{
        id = Integer.parseInt(c.getString(1));
        String name = c.getString(0);
        String expiryDate = c.getString(2);
      }while(c.moveToNext());
    }
    c.close();
    db.close();
  }

  public static Product getProduct(String gtin) throws SQLException {
    /**
     *
     * RawQuery: Runs the provided SQL
     */
    Product p1 = new Product(gtin,KEY_NAME,KEY_EXPIRYDATE);
    Statement stmt = null;
    try {
      DatabaseHandler.openConnection();

      stmt = c.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    ResultSet rs = stmt.executeQuery("SELECT name FROM Product WHERE name = "+KEY_NAME);

    if(rs.first()){
      do{
        gtin = rs.getString(1);
        String name = rs.getString(0);
        String expiryDate = rs.getString(2);
      }while(rs.next());
    }
    p1.setGTIN(rs.getString(0));
    p1.setName(rs.getString(1));
    p1.setExpiryDate(rs.getDate(2));
    db.close();

    return p1;
  }

  // Getting All Products
  public static Product getAllProducts(String name, String gtin, Date expiryDate) throws SQLException {
    Product p1 = new Product(name, gtin,expiryDate);
    Statement stmt = null;
    try {
      stmt = c.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    ResultSet rs = stmt.executeQuery("SELECT Lname FROM Customers WHERE Snum = 2001");

    if (rs.first()) { // data?
      do{
        name = rs.getString(rs.getString("name"));
        gtin = rs.getString(rs.getString("gtin"));
        expiryDate = rs.getDate(String.valueOf(rs.getDate("expiryDate")));
      }while(rs.next());
    }
    rs.close();
    db.close();
    return p1;

  }

  // Updating single Product

  /**
   * this method updates a Product with the new given values
   *
   * @param gtin
   * @param setGTIN
   * @param setName
   * @param setExpiryDate
   */
  public void updateProduct(String gtin, String setGTIN, String setName, String setExpiryDate) {
    try {
      db.execSQL("UPDATE Product"
          + "SET setGTIN = "+setGTIN+" ,setName = "+setName+" ,setExpiryDate = "+setExpiryDate
          + " FROM Products where TRIM(gtin)=" + gtin.trim());
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
    }
    System.out.println("Updating of Product successfull");
  }

  // Deleting single Product
  public void deleteProduct(String gtin) {

    try {
      db.execSQL("DELETE FROM Products where TRIM(gtin)="+gtin.trim());
    } catch ( Exception e ) {
      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
    }
    System.out.println("Deleting of Product successfull");
  }
}

