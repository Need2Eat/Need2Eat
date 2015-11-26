package at.need2eat.need2eat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.Image;

import java.util.List;

/**
 * Created by Tomi on 25.11.2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
  private static final int DATABASE_VERSION = 1;

  // Database Name
  private static final String DATABASE_NAME = "Products";

  // Contacts table name
  private static final String TABLE_PRODUCTS = "products";

  private static Image image;
  // Contacts Table Columns names
  private static final String KEY_GTIN = "gtin";
  private static final String KEY_NAME = "name";
  private static final Image KEY_IMAGE = image;

  public DatabaseHandler(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  // Creating Tables
  public void onCreate(SQLiteDatabase db) {
    String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
        + KEY_GTIN+ " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
        + KEY_IMAGE + " TEXT" + ")";
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
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(KEY_NAME, product.getName());
    values.put(KEY_IMAGE, product.getImage());

    // Inserting Row
    db.insert(TABLE_PRODUCTS, null, values);
    db.close();
  }

  // Getting single contact
  public Product getProduct(int gtin) {

  }

  // Getting All Products
  public List<Product> getAllProducts() {

  }

  // Getting Product Count
  public int getProductsCount() {

  }
  // Updating single Product
  public int updateProduct(Product product) {

  }

  // Deleting single Product
  public void deleteProduct(Product product) {

  }
}

