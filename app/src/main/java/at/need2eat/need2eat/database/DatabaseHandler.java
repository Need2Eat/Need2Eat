package at.need2eat.need2eat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.Image;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomi on 25.11.2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

  /**
   * In this method(the main-method) a try-catch block is given, which
   * tries to connect to the internal database "n2e.db"(we don't have one,
   * so we maybe have to create a new on) and if it succeeded, a message is been printed
   * @param args
   */
  public static void main( String args[] )
  {
    Connection c = null;
    try {
      Class.forName("org.sqlite.JDBC");
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

  // Contacts table name
  private static final String TABLE_PRODUCTS = "products";

  private static Image image;
  // Contacts Table Columns names
  private static final String KEY_GTIN = "gtin";
  public static int KEY_ID;
  public static String KEY_NAME = "name";
  public static String KEY_EXPIRYDATE = "expiryDate";

  public DatabaseHandler(Context context) {

    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  SQLiteDatabase db;
  // Creating Tables
  public void onCreate(SQLiteDatabase db) {
    String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
        + KEY_GTIN+ " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"+
        KEY_EXPIRYDATE+"TEXT"+")";
    db.execSQL(CREATE_PRODUCTS_TABLE);
  }

  // Upgrading database
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Drop older table if existed
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);

    // Create tables again
    onCreate(db);
  }
  // Adding new contact
  public void addProduct(Product product) {
    db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(KEY_NAME, product.getName());

    // Inserting Row
    db.insert(TABLE_PRODUCTS, null, values);
    db.close();
  }

  // Getting single Product
  public Product getProduct(String gtin) {
    Cursor c = db.rawQuery("SELECT * FROM Products WHERE TRIM(gtin) = '"+gtin.trim()+"'", null);
    if(c.moveToFirst()){
      do{
        //assing values
        gtin = c.getString(1);
        String name = c.getString(0);
        String expiryDate = c.getString(2);
        //Do something Here with values
      }while(c.moveToNext());
    }
    c.close();
    db.close();
    return (Product) c;
  }

  // Getting All Products
  public Product getAllProducts(String name, String gtin, String expiryDate) {
    Cursor cursor = db.rawQuery("SELECT * FROM Products", null);

    if (cursor.moveToFirst()) // data?
      name = cursor.getString(cursor.getColumnIndex("name"));
      gtin = cursor.getString(cursor.getColumnIndex("gtin"));
      expiryDate = cursor.getString(cursor.getColumnIndex("expiryDate"));
    cursor.close();
    return (Product) cursor;

  }

  // Getting Product Count
  public int getProductsCount() {
    return 1;

  }
  // Updating single Product
  public int updateProduct(Product product) {
    return 1;

  }

  // Deleting single Product
  public void deleteProduct(Product product) {

  }
}

