package at.need2eat.need2eat.DatabaseHandlerTest;

import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;

import org.json.JSONException;

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
    Product p1 = DatabaseHandler.getProduct("5900190004954");
    assertEquals("5900190004954",p1);
  }
  @SmallTest
  public void testDatabaseHandler2() throws JSONException {
    Product p2 = DatabaseHandler.getAllProducts("KRAKUS", "5900190004954", "22-04-2017");
    assertEquals("{gtin: 5900190004954, name:KRAKUS, expiryDate: 22-04-2017}", p2);
  }

  @SmallTest
  public void testDatabaseHandler3() {
    Product p3 = DatabaseHandler.getProduct("1");
    assertEquals("1",p3);
  }
  @SmallTest
  public void testDatabaseHandler4() throws  Exception{
    //Product p4 = DatabaseHandler.getProductAttributes("0123456789");
    //assertEquals("0123456789",p4.toString());
  }


  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }
}
