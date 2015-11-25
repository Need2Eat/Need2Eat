package at.need2eat.need2eat.database;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.*;
/**
 * Created by Tomi on 25.11.2015.
 */
public class OutpanObject {
  public String
      gtin,
      outpan_url,
      name;

  public HashMap<String, String>
      attributes;

  public ArrayList<String>
      images,
      videos;

  public OutpanObject() {
    this.gtin = "";
    this.outpan_url = "";
    this.name = "";

    this.attributes = new HashMap<String, String>();
    this.images = new ArrayList<String>();
    this.videos = new ArrayList<String>();
  }
  public static String[] getNames(JSONObject jo) {
    int length = jo.length();
    if (length == 0) {
      return null;
    }
    Iterator iterator = jo.keys();
    String[] names = new String[length];
    int i = 0;
    while (iterator.hasNext()) {
      names[i] = (String)iterator.next();
      i += 1;
    }
    return names;
  }
  public OutpanObject(JSONObject json) throws JSONException {
    this();

    this.gtin = json.getString("gtin");
    this.outpan_url = json.getString("outpan_url");

    if (!json.isNull("name"))
      this.name = json.getString("name");

    if (!json.isNull("attributes")) {
      JSONObject attrObject = json.getJSONObject("attributes");
      String[] attrs = OutpanObject.getNames(attrObject);

      for (int a = 0; a < attrs.length; a++)
        this.attributes.put(attrs[a], attrObject.getString(attrs[a]));
    }

    if (!json.isNull("images")) {
      JSONArray imgs = json.getJSONArray("images");
      for (int i = 0; i < imgs.length(); i++)
        this.images.add(imgs.getString(i));
    }

    if (!json.isNull("videos")) {
      JSONArray vids = json.getJSONArray("videos");
      for (int i = 0; i < vids.length(); i++)
        this.videos.add(vids.getString(i));
    }
  }
}
