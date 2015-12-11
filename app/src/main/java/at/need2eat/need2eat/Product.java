package at.need2eat.need2eat;

import org.json.JSONObject;

/**
 * This class contains information about the product and a ID, which makes every product unique.
 * @author Sebastian Feistl
 */
public class Product {

  /**
   * ID (Primary Key) which identifies the product
   */
  private int id;

  /**
   * Product barcode
   */
  private String gtin;

  /**
   * Product name
   */
  private String name;

  /**
   * Expiry date of the product
   */
  private String expiryDate;

  /**
   * Constructs a new Product with the name of the Product as a string and the expiryDate
   * @param name
   * @param expiryDate
   */
  public Product(String name, String expiryDate) {
    this.name = name;
    this.expiryDate = expiryDate;
  }
  /**
   * Constructs a new Product with a barcode (GTIN) as String, a name and expiryDate
   * @param gtin is the barcode of the product
   * @param name of the product
   * @param expiryDate of the product
   */
  public Product(String gtin, String name, String expiryDate) {
    this.gtin = gtin;
    this.name = name;
    this.expiryDate = expiryDate;
  }

  /**
   * Constructs a new Product with a unique ID, a barcode (GTIN) as String, a name and expiryDate
   * @param id is a unique number which identifies the product
   * @param gtin is the barcode of the product
   * @param name of the product
   * @param expiryDate of the product
   */
  public Product(int id, String gtin, String name, String expiryDate) {
    this.id = id;
    this.gtin = gtin;
    this.name = name;
    this.expiryDate = expiryDate;
  }

  /**
   * Get the ID of the product which identifies the product uniquely
   * @return the product ID
   */
  public int getID() {
    return this.id;
  }

  /**
   * Set the ID of the product which identifies the product uniquely
   * @param id the product id
   */
  public void setID(int id) {
    this.id = id;
  }

  /**
   * Get the GTIN, the actual barcode as String, of the product
   * @return the product GTIN
   */
  public String getGTIN() {
    return this.gtin;
  }

  /**
   * Set the GTIN, the actual barcode as String, of the product
   * @param gtin the product GTIN
   */
  public void setGTIN(String gtin) {
    this.gtin = gtin;
  }

  /**
   * Get the name of the product
   * @return the product name
   */
  public String getName() {
    return this.name;
  }

  /**
   * Set the name of the product
   * @param name the product name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Get the expiry date of the product
   * @return the expiry date of the product
   */
  public String getExpiryDate() {
    return this.expiryDate;
  }

  /**
   * Set the expiry date of the product
   * @param expiryDate the expiry date of the product
   */
  public void setExpiryDate(String expiryDate) {
    this.expiryDate = expiryDate;
  }

}