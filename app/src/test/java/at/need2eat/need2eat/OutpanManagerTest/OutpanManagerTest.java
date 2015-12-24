package at.need2eat.need2eat.OutpanManagerTest;

import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;

import at.need2eat.need2eat.database.OutpanManager;



/**
 * @author Tomi Mijatovic
 */
public class OutpanManagerTest extends TestCase {
  private static String api_key = "21695344493be75568b4c42ef6b80d99";

  OutpanManager manager = new OutpanManager(api_key);

  protected void setUp() throws Exception {
    super.setUp();
  }

  @SmallTest
  public void testOutpan1() {;
    assertEquals("", manager.getName("9002244132272").toLowerCase());
  }

  @SmallTest
  public void testOutpan2() {
    assertEquals("krakus", manager.getName("5900190004954").toLowerCase());
  }

  @SmallTest
  public void testOutpan3() {
    assertEquals("rauch eistee zitrone", manager.getName("90020742").toLowerCase());
  }

  @SmallTest
  public void testOutpan4() {
    assertEquals("bahlsen lebkuchen herzen und sterne", manager.getName("4017100851919").toLowerCase());
  }

  @SmallTest
  public void testOutpan5() {
    assertEquals("", manager.getName(null));
  }

  protected void tearDown() throws Exception {
    super.tearDown();
  }


}
