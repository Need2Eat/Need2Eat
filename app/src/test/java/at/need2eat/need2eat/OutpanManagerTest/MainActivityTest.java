package at.need2eat.need2eat.OutpanManagerTest;

import android.test.ActivityInstrumentationTestCase2;

import at.need2eat.need2eat.MainActivity;

/**
 * Created by Tomi on 11.12.2015.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {


  MainActivity mainActivity;

  public MainActivityTest() {
    super(MainActivity.class);
  }



  @Override
  protected void setUp() throws Exception {
    super.setUp();
    mainActivity = getActivity();
  }


}
