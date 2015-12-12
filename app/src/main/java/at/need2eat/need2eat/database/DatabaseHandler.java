package at.need2eat.need2eat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.Image;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;


import at.need2eat.need2eat.Product;

/**
 * Created by Tomi on 25.11.2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper implements DatabaseManager{

  /**
   * This "openConnection"-Method creates a connection
   * to our internal database
   */
    public void openConnection(){
      Connection c = null;
      try {
        Class.forName("org.sqlite.JDBC");
        /**
         * n2e.db is our internal database(name)
         * BUT WE DON'T HAVE ONE EITHERWAY
         */
        c = DriverManager.getConnection("jdbc:sqlite:n2e.db");
      } catch ( Exception e ) {
        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        System.exit(0);
      }
      System.out.println("Opened database successfully");
    }
  private static final int DATABASE_VERSION = 1;

  // Database Name
  private static final String DATABASE_NAME = "Products";

  // Products table name
  private static final String TABLE_PRODUCTS = "products";

  private static Image image;
  // Products Table Columns gtin, id, expiryDate
  private static final String KEY_GTIN = "gtin";
  public static int KEY_ID;
  public static String KEY_NAME = "name";
  public static String KEY_EXPIRYDATE = "expiryDate";

  public DatabaseHandler(Context context) {

    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  public static SQLiteDatabase db;
  // Creating Tables
  public void onCreate(SQLiteDatabase db) {
    String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
        + KEY_GTIN+ " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"+
        KEY_EXPIRYDATE+"TEXT"+
        KEY_ID+ " INT auto_increment"+")";
    db.execSQL(CREATE_PRODUCTS_TABLE);
  }

  // Upgrading database
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Drop older table if existed
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);

    // Create tables again
    onCreate(db);
  }

    public Product getProduct(int id) {
    KEY_ID = id;
    return new Product(id, KEY_GTIN, KEY_NAME, KEY_EXPIRYDATE);
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

  public static Product getProduct(String gtin) {
    /**
     * Cursor: This interface provides random read-write access
     * to the result set returned by a database query
     *
     * RawQuery: Runs the provided SQL and returns a Cursor over the result set
     */

    Cursor c = db.rawQuery("SELECT * FROM Products WHERE TRIM(gtin) = '"+gtin.trim()+"'", null);
    if(c.moveToFirst()){
      do{
        gtin = c.getString(1);
        String name = c.getString(0);
        String expiryDate = c.getString(2);
      }while(c.moveToNext());
    }
    c.close();
    db.close();
    return (Product) c;
  }

  // Getting All Products
  public static Product getAllProducts(String name, String gtin, String expiryDate) {
    Cursor cursor = db.rawQuery("SELECT * FROM Products", null);

    if (cursor.moveToFirst()) { // data?
      do{
        name = cursor.getString(cursor.getColumnIndex("name"));
        gtin = cursor.getString(cursor.getColumnIndex("gtin"));
        expiryDate = cursor.getString(cursor.getColumnIndex("expiryDate"));
      }while(cursor.moveToNext());
    }
    cursor.close();
    db.close();
    return (Product) cursor;

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

