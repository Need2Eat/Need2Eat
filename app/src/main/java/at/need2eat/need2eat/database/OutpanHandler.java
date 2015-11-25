package at.need2eat.need2eat.database;

/**
 * Created by Tomi on 18.11.2015.
 */
public interface OutpanHandler {
  //Methods for Outpan
  //SELECT --> gives us the Productname
  // by the Product from the database OUTPAN
  //SELECT
  public String select(String gtin);

}
