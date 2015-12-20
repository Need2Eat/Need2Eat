package at.need2eat.need2eat.DatabaseHandlerTest;

import android.content.Context;
import android.provider.ContactsContract;
import android.test.mock.MockContext;
import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;

import org.json.JSONException;

import java.sql.SQLException;
import java.util.Date;

import at.need2eat.need2eat.Product;
import at.need2eat.need2eat.database.DatabaseHandler;
import at.need2eat.need2eat.database.OutpanManager;

/**
 * Created by Tomi on 11.12.2015.
 */
public class DatabaseHandlerTest extends TestCase{
  DatabaseHandler db1 = new DatabaseHandler(new MockContext());
  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }

  /*@SmallTest
  public void testDatabaseHandler1(){
    Product p1 = null;
    try {
      p1 = db1.getProduct("5900190004954");
    } catch (SQLException e) {
      e.printStackTrace();
    }
    assertEquals("5900190004954", p1.getGTIN());
  }*/

  /*@SmallTest
  public void testDatabaseHandler2() throws JSONException {
    Product p2 = null;
    try {
      p2 = db1.getAllProducts("KRAKUS", "5900190004954", new Date(2017, 04, 22));
    } catch (SQLException e) {
      e.printStackTrace();
    }
    assertEquals("{gtin: 5900190004954, name:KRAKUS, expiryDate: 22-04-2017}", p2);
  }*/

    /*@SmallTest
    public void testDatabaseHandler3() {
      Product p3 = null;
      try {
        p3 = db1.getProduct("1");
      } catch (SQLException e) {
        e.printStackTrace();
      }
      assertEquals("1",p3.getID());
    }*/


  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }
}
