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

public class GetterOutpan {

  /**
   * This is our api_key from the Outpan DB
   */
  private String api_key = "21695344493be75568b4c42ef6b80d99";
  String code;

  /**
   * this Contructor sets this api key(see 'private string api_key') from our Outpan DB
   * to the included api_key
   *
   * @param api_key
   */
  public GetterOutpan(String api_key) {
    this.api_key = api_key;
  }

  /**
   * this method with the returning Value as a JSONObject
   * it runs the method executeGet(see down below) with the gtin key,
   * which is the "barcode", and parses into a string ""
   *
   * @param gtin
   * @return
   */
  private JSONObject executeGet(String gtin) {

    return executeGet(gtin, "");
  }

  /**
   * this private method executeGet does a try-do-connection to connect with
   * the outpan DB with our api_key and the gtin string.
   * Therefore, in case the connection fails, a ErrorException is placed to catch the Exception
   *
   * @param gtin
   * @param endpoint
   * @return
   */
  private JSONObject executeGet(String gtin, String endpoint) {
    code = gtin;
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
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return jsonResult;
  }

  /**
   * these methods get the name, the attributes and the images of this product;
   *
   * @param gtin
   * @return
   * @throws JSONException
   */
  public Product getProduct(String gtin) throws JSONException {
    return new Product(executeGet(gtin).toString());
  }

  public Product getProductName(String gtin) throws JSONException{
    return new Product(executeGet(gtin, "/name").toString());
  }

  public Product getProductAttributes(String gtin) throws JSONException{
    return new Product(gtin,"Testproduct","02.12.2015");
  }

}