package at.need2eat.need2eat.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


import at.need2eat.need2eat.Product;
import at.need2eat.need2eat.util.DateConverter;
import at.need2eat.need2eat.util.LogUtils;

/**
 * @author Tomi Mijatovic (until 20.12.2015)
 * @author Maxi Nothnagel - mx.nothnagel@gmail.com (starting 20.12.2015)
 */
public class DatabaseHandler extends SQLiteOpenHelper implements DatabaseManager {

  /**
   * This enum provides statically defined column names of the SQLite database
   * @author Maxi Nothnagel - mx.nothnagel@gmail.com
   */
  private enum ColumnName implements BaseColumns {
    GTIN("gtin"),
    PRODUCTNAME("productname"),
    EXPIRY_DATE("expiryDate");

    private String name;

    /**
     * Creates a new {@code ColumnName} with the given name
     * @param name the name of the column
     */
    ColumnName(String name) {
      this.name = name;
    }

    /**
     * Returns the name of the column as a {@code String}
     * @return the name of the column
     */
    public String getName() {
      return this.name;
    }
  }

  /**
   * This enum provides statically defined SQL statements or parts of SQL statements
   * @author Maxi Nothnagel - mx.nothnagel@gmail.com
   */
  private enum SqlStatements {
    CREATE("CREATE TABLE IF NOT EXISTS %s ("
        + "%s int auto_increment primary key, %s varchar(400) not null,"
        + "%s varchar(100), %s char(10))"),
    DROP("DROP TABLE IF EXISTS %s"),
    WHERE("%s = %d");

    private String stm;

    /**
     * Creates a new statement with the given {@code String} as the statement
     * @param stm the SQL statement
     */
    SqlStatements(String stm) {
      this.stm = stm;
    }

    /**
     * Returns the statement as a {@code String}
     * @return the statement
     */
    public String getStatement() {
      return this.stm;
    }
  }

  // The Context of the Database
  private Context context;

  // The information about the database
  private static final String DATABASE_NAME = "Need2Eat";
  private static final String TABLE_NAME = "Product";
  private static final int VERSION = 1;

  /**
   * Creates a new {@code DatabaseHandler} with the given {@link Context}
   * @param context the {@code Context} to use, create or open the database
   */
  public DatabaseHandler(Context context) {
    super(context, DatabaseHandler.DATABASE_NAME, null, DatabaseHandler.VERSION);
    this.context = context;
  }

  // SQLiteOpenHelper Methods
  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(String
        .format(SqlStatements.CREATE.getStatement(), DatabaseHandler.TABLE_NAME, ColumnName._ID,
            ColumnName.PRODUCTNAME, ColumnName.GTIN, ColumnName.EXPIRY_DATE));
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Drop old tables and create the new ones
    db.execSQL(String.format(SqlStatements.DROP.getStatement(), DatabaseHandler.TABLE_NAME));
    onCreate(db);
  }

  // DatabaseManager Methods
  @Override
  public List<Product> getAllProducts(boolean sorted) {
    List<Product> result = new LinkedList<>();

    try (SQLiteDatabase database = getReadableDatabase();
         Cursor c = database.query(DatabaseHandler.TABLE_NAME,
             new String[]{ColumnName.GTIN.getName(), ColumnName.PRODUCTNAME
                 .getName(), ColumnName.EXPIRY_DATE.getName()},
             null, null, null, null, null)) {

      /*
      Iterate over the output rows and create a new product for every row using the information
      from the columns
       */
      while (c.moveToNext()) {
        int id = c.getInt(c.getColumnIndex(ColumnName._ID));
        String gtin = c.getString(c.getColumnIndex(ColumnName.GTIN.getName()));
        String name = c.getString(c.getColumnIndex(ColumnName.PRODUCTNAME.getName()));
        Date date = null;
        try {
          date = DateConverter.getDateFromString(c.getString(
              c.getColumnIndex(ColumnName.EXPIRY_DATE.getName())));
        } catch (ParseException e) {
          /*
          If the date from the database could not be converted to a Date. However,
          this catch block should not be reachable as the dates are added directly
          from the EditActivity
           */
        }

        result.add(new Product(id, gtin, name, date));
      }
    } catch (SQLiteException e) {
      LogUtils.logError(context, "DatabaseHandler", "Verbindung zur Datenbank gescheitert!", e);
    }

    if (sorted) {
      /*
      Sort the list of products by their expiry date (products which expire earlier are more
      important)
       */
      Collections.sort(result, new Comparator<Product>() {
        @Override
        public int compare(Product product1, Product product2) {
          return product1.getDaysUntilExpiry() - product2.getDaysUntilExpiry();
        }
      });
    }

    return result;
  }

  @Override
  public void addProduct(Product newProduct) {

  }

  @Override
  public void updateProduct(Product newProduct) {

  }

  @Override
  public void deleteProduct(int id) {

  }
   /* static Connection c = null;

  *//**
   * This "openConnection"-Method creates a connection
   * to our internal database
   *//*
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
  public static String date = "expiryDate";

  public DatabaseHandler(Context context) {

    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  public static SQLiteDatabase db;

  // Creating Tables
  public void onCreate(SQLiteDatabase db) {
    String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
        + KEY_GTIN+ " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"+
        date+" DATE,"+
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
    Cursor c = db.rawQuery("DELETE * FROM Products WHERE id=" + id, null);
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

  public Product getProduct(String gtin) throws SQLException {
    *//**
     *
     * RawQuery: Runs the provided SQL
     *//*
    Product p1 = new Product(gtin,KEY_NAME,KEY_EXPIRYDATE);
    Statement stmt = null;
    try {
      openConnection();
      onCreate(db);
      stmt = c.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    ResultSet rs = stmt.executeQuery("SELECT name FROM Product WHERE gtin = "+gtin);

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
  public Product getAllProducts(String name, String gtin, Date expiryDate) throws SQLException {
    Product p1 = new Product(name, gtin,expiryDate);
    Statement stmt = null;
    try {
      DatabaseHandler.openConnection();
      onCreate(db);
      stmt = c.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    ResultSet rs = stmt.executeQuery("SELECT name FROM Product WHERE name = "+name);

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

  *//**
   * this method updates a Product with the new given values
   *
   * @param gtin
   * @param setGTIN
   * @param setName
   * @param setExpiryDate
   *//*
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
  }*/
}

