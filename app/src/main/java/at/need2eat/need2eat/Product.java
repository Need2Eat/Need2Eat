package at.need2eat.need2eat;
import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import at.need2eat.need2eat.util.DateConverter;

/**
 * This class contains information about the product and a ID, which makes every product unique.
 * @author Sebastian Feistl
 */
public class Product implements Serializable {

  public static final long serialVersionUID = 142L;

  /**
   * ID (Primary Key) which identifies the product
   * @serial unique id
   */
  private int id;

  /**
   * Product barcode
   * @serial gtin string
   */
  private String gtin;

  /**
   * Product name
   * @serial name string
   */
  private String name;

  /**
   * Expiry date of the product
   * @serial expiry date as date
   */
  private Date expiryDate;

  /**
   * Constructs a new Product with the name of the Product as a string and the expiryDate
   * @param name of the product
   * @param expiryDate of the product
   */
  public Product(String name, Date expiryDate) {
    this.name = name;
    this.expiryDate = expiryDate;
  }
  /**
   * Constructs a new Product with a barcode (GTIN) as String, a name and expiryDate
   * @param gtin is the barcode of the product
   * @param name of the product
   * @param expiryDate of the product
   */
  public Product(String gtin, String name, Date expiryDate) {
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
  public Product(int id, String gtin, String name, Date expiryDate) {
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
  public Date getExpiryDate() {
    return this.expiryDate;
  }

  /**
   * Get this Product's expiry date as a String.
   * @return a String representation of this Product's expiry date
   */
  public String getExpiryDateString() {
    return DateConverter.getStringFromDate(expiryDate);
  }

  /**
   * Set the expiry date of the product
   * @param expiryDate the expiry date of the product
   */
  public void setExpiryDate(Date expiryDate) {
    this.expiryDate = expiryDate;
  }

  /**
   * Gets the number of days until this Product's expiry.
   * @return the rounded up difference between this Product's expiry date and the current time in
   * days
   */
  public int getDaysUntilExpiry() {
    long millis = expiryDate.getTime() - new Date().getTime();
    return (int)(TimeUnit.DAYS.convert(millis, TimeUnit.MILLISECONDS)) + 1;
  }

}