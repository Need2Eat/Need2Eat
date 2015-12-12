package at.need2eat.need2eat.OutpanManagerTest;

import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;

import at.need2eat.need2eat.Product;
import at.need2eat.need2eat.database.OutpanManager;
import static org.junit.Assert.assertEquals;

import org.json.JSONException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 * Created by Tomi on 11.12.2015.
 */
public class OutpanManagerTest extends TestCase {


  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }




    @SmallTest
    public void testOutpanManager1(){
      Product p1 = OutpanManager.getProduct("5900190004954","KRAKUS");
      assertEquals("5900190004954,KRAKUS", p1);
    }
    @SmallTest
    public void testOutpanManager2() {
      Product p2 = null;
      try {
        p2 = OutpanManager.getProductName("5900190004954", "22-04-2017");
      } catch (JSONException e) {
        e.printStackTrace();
      }
      assertEquals("{gtin: 5900190004954,expiryDate: 22-04-2017}", p2);
    }

  @SmallTest
  public void testOutpanManager3() {
    Product p3 = OutpanManager.getProduct("0123456789", "Dunno");
    assertEquals("0123456789",p3.getName());
  }
  @SmallTest
  public void testOutpanManager4() {
    Product p4 = null;
    try {
      p4 = OutpanManager.getProductAttributes("0123456789");
    } catch (JSONException e) {
      e.printStackTrace();
    }
    assertEquals("0123456789",p4.getGTIN());
  }


    @Override
    protected void tearDown() throws Exception {
      super.tearDown();
    }
}
