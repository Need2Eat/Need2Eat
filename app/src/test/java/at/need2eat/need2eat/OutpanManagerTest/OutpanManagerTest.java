package at.need2eat.need2eat.OutpanManagerTest;

import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;

import at.need2eat.need2eat.Product;
import at.need2eat.need2eat.database.OutpanHandler;
import at.need2eat.need2eat.database.OutpanManager;
/**
 * import static org.junit.Assert.assertEquals;

 import org.json.JSONException;
 import org.junit.AfterClass;
 import org.junit.BeforeClass;
 import org.junit.Test;
 */



/**
 * Created by Tomi on 11.12.2015.
 */
public class OutpanManagerTest extends TestCase {
  private static String api_key = "21695344493be75568b4c42ef6b80d99";

  OutpanManager om1 = new OutpanManager(api_key);

  protected void setUp() throws Exception {
    super.setUp();
  }

    @SmallTest
    public void testOutpanManager1(){
      String p1 = om1.getName("5900190004954");
      assertEquals("KRAKUS", p1);
    }
    @SmallTest
    public void testOutpanManager2(){
      String p1 = om1.getName("5099837089798");
      assertEquals("Jack Daniels", p1);
    }
    @SmallTest
    public void testOutpanManager3(){
      String p1 = om1.getName("43882236");
      assertEquals("MC Illroy", p1);
    }
    @SmallTest
    public void testOutpanManager4(){
      String p1 = om1.getName("01781761414");
      assertEquals("BOSE QC3", p1);
    }

    protected void tearDown() throws Exception {
      super.tearDown();
    }


}
