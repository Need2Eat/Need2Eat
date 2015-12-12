package at.need2eat.need2eat.database;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import at.need2eat.need2eat.Product;

/**
 * Created by Tomi on 18.11.2015.
 */

public class OutpanManager implements OutpanHandler {

  /**
   * This is our api_key from the Outpan DB
   */
  private static String api_key = "21695344493be75568b4c42ef6b80d99";
  public static String code;
  public static String name;

  /**
   * this Contructor sets this api key(see 'private string api_key') from our Outpan DB
   * to the included api_key
   *
   * @param api_key
   */
    public OutpanManager(String api_key) {
      this.api_key = api_key;
    }

  public static String getName(String gtin) {
    code = gtin;
    String name = "";
    JSONObject jsonResult = new JSONObject();

    try {
      /** defines the url, where it has to go */
      URL url = new URL("https://api.outpan.com/v2/products/"+code+"?apikey="+api_key);
      /** creates the connection and "opens" it */
      URLConnection uc = url.openConnection();

      String key = api_key + ":";

      /** connects with this network connection */

      int numCharsRead;
      char[] charArray = new char[1024];
      StringBuffer sb = new StringBuffer();

      jsonResult = new JSONObject(sb.toString());
      name = jsonResult.toString();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return name;
  }
}