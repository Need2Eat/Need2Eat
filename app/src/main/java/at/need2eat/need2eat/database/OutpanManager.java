package at.need2eat.need2eat.database;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * This class represents the connection to the database of Outpan
 * @author Tomi Mijatovic (until 19.12.2015)
 * @author Maxi Nothnagel - mx.nothnagel@gmail.com (starting 19.12.2015)
 * @author Patrick Taibel
*/
public class OutpanManager implements OutpanHandler {
  // The API key of Outpan
  private String apiKey;

  /**
   * Creates a new OutpanManager with the specific API key
   * @param apiKey the {@code String} that represents the API key to connect to the Outpan database
   */
  public OutpanManager(String apiKey) {
    this.apiKey = apiKey;
  }

  @Override
  public String getName(String gtin) {
    /*
    These values must be declared before the try statement in order to close/disconnect them in the
    finally block as HttpsURLConnection is not AutoClosable
     */
    HttpsURLConnection connection = null;
    InputStream in = null;

    // The JSONObject that represents the result of the GET request
    JsonObject result = null;

    try {
      /*
      Create a new HttpsURLConnection to Outpan. This has to be a HttpsURLConnection as Outpan
      only accepts HTTPs GET requests
       */
      connection = (HttpsURLConnection) new URL(String.format(
        "https://api.outpan.com/v2/products/%s?apikey=%s", gtin, apiKey)).openConnection();

      in = connection.getInputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));

      StringBuilder output = new StringBuilder();
      String line;

      // Read the output which is a JSON Object line by line and append it to the StringBuilder
      while ((line = reader.readLine()) != null) {
        output.append(line).append("\n");
      }

      // Create a JsonObject using the output as a String
      result = new JsonParser().parse(output.toString()).getAsJsonObject();
    } catch (IOException e) {
      return "";
      /*
      If any of these Exceptions occur, an empty String is returned to represent that nothing
      could be found
       */
    } finally {
      // Close the InputStream and disconnect the connection (if they are not null)
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
          /*
          This catch block can be empty as if closing the connection fails, one can assume
          that it has already been closed
            */
        }
      }

      if (connection != null) {
        connection.disconnect();
      }
    }

    // Return the name of the product or an empty String if the name could not be found
    return result.get("name").isJsonNull() ? "" : result.get("name").getAsString();
  }
}