package at.need2eat.need2eat.DatabaseHandlerTest;

import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;

import org.json.JSONException;

import java.sql.SQLException;

import at.need2eat.need2eat.Product;
import at.need2eat.need2eat.database.DatabaseHandler;
import at.need2eat.need2eat.database.OutpanManager;

/**
 * Created by Tomi on 11.12.2015.
 */
public class DatabaseHandlerTest extends TestCase{

  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }

  @SmallTest
  public void testDatabaseHandler1(){
    Product p1 = null;
    try {
      p1 = DatabaseHandler.getProduct("5900190004954");
    } catch (SQLException e) {
      e.printStackTrace();
    }
    assertEquals("5900190004954",p1.getGTIN());
  }
  @SmallTest
  public void testDatabaseHandler2() throws JSONException {
    Product p2 = null;
    try {
      p2 = DatabaseHandler.getAllProducts("KRAKUS", "5900190004954", "22-04-2017");
    } catch (SQLException e) {
      e.printStackTrace();
    }
    assertEquals("{gtin: 5900190004954, name:KRAKUS, expiryDate: 22-04-2017}", p2);
  }

  @SmallTest
  public void testDatabaseHandler3() {
    Product p3 = null;
    try {
      p3 = DatabaseHandler.getProduct("1");
    } catch (SQLException e) {
      e.printStackTrace();
    }
    assertEquals("1",p3.getID());
  }
  @SmallTest
  public void testDatabaseHandler4(){
    //Product p4 = DatabaseHandler.getProductAttributes("0123456789");
    //assertEquals("0123456789",p4.toString());
  }


  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }
}
