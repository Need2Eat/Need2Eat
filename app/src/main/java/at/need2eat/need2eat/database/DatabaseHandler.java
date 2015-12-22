package at.need2eat.need2eat.database;

import android.content.ContentValues;
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
    GTIN, PRODUCTNAME, EXPIRY_DATE;

    @Override
    public String toString() {
      return name().toLowerCase();
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

    public String with(Object... args) {
      return String.format(stm, args);
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
    db.execSQL(SqlStatements.CREATE.with(DatabaseHandler.TABLE_NAME,
        ColumnName._ID, ColumnName.PRODUCTNAME, ColumnName.GTIN, ColumnName.EXPIRY_DATE));
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Drop old tables and create the new ones
    db.execSQL(SqlStatements.DROP.with(DatabaseHandler.TABLE_NAME));
    onCreate(db);
  }

  // DatabaseManager Methods
  @Override
  public List<Product> getAllProducts(boolean sorted) {
    List<Product> result = new LinkedList<>();

    try (SQLiteDatabase database = getReadableDatabase();
         Cursor c = database.query(DatabaseHandler.TABLE_NAME,
             new String[]{ColumnName.GTIN.toString(), ColumnName.PRODUCTNAME.toString(),
                 ColumnName.EXPIRY_DATE.toString()}, null, null, null, null, null)) {

      /*
      Iterate over the output rows and create a new product for every row using the information
      from the columns
       */
      while (c.moveToNext()) {
        int id = c.getInt(c.getColumnIndex(ColumnName._ID));
        String gtin = c.getString(c.getColumnIndex(ColumnName.GTIN.toString()));
        String name = c.getString(c.getColumnIndex(ColumnName.PRODUCTNAME.toString()));
        Date date = null;
        try {
          date = DateConverter.getDateFromString(c.getString(
              c.getColumnIndex(ColumnName.EXPIRY_DATE.toString())));
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

  private ContentValues getValuesFromProduct(Product product) {
    ContentValues content = new ContentValues();
    String[] keys = new String[] {
        ColumnName.GTIN.toString(), ColumnName.EXPIRY_DATE.toString(),
        ColumnName.PRODUCTNAME.toString()
    };
    String[] values = new String[] {
        product.getGTIN(), product.getExpiryDateString(), product.getName()
    };
    for (int i = 0; i < keys.length; i++) {
      content.put(keys[i], values[i]);
    }
    return content;
  }

  @Override
  public void addProduct(Product newProduct) {
    SQLiteDatabase db = getWritableDatabase();
    db.insert(TABLE_NAME, null, getValuesFromProduct(newProduct));
    db.close();
  }

  @Override
  public void updateProduct(Product newProduct) {
    SQLiteDatabase db = getWritableDatabase();
    String where = SqlStatements.WHERE.with(ColumnName._ID, newProduct.getID());
    db.update(TABLE_NAME, getValuesFromProduct(newProduct), where, null);
    db.close();
  }

  @Override
  public void deleteProduct(int id) {

  }
}
