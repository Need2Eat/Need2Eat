package at.need2eat.need2eat.database;

import android.media.Image;
import android.widget.ImageView;

/**
 * Created by Tomi on 25.11.2015.
 */
public class Product {

  //private variables
  int gtin;
  String name;
  Image image;

  // Empty constructor
  public Product(){

  }
  // constructor
  public Product(int gtin, String name){
    this.gtin = gtin;
    this.name = name;
  }

  // constructor
  public Product(int gtin, Image image){
    this.gtin = gtin;
    this.image = image;
  }

  // getting GTIN
  public int getGTIN(){
    return this.gtin;
  }

  // setting GTIN
  public void setGTIN(int gtin){
    this.gtin = gtin;
  }
  // getting name
  public String getName(){

    return this.name;
  }

  // setting name
  public void setName(String name){
    this.name = name;
  }

}
