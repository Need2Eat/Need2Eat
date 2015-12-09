package at.need2eat.need2eat.database;

import java.util.List;

import at.need2eat.need2eat.Product;

/**
 * Created by AM307 on 09.12.2015.
 */
public interface DatabaseManager {

  Product getProduct(int id);

  List<Product> getAllProducts();

  void addProduct(Product newProduct);

  /**
   * Information about the product (e.g. id of the Product to update) can be accessed through
   * newProduct
   * @param newProduct Contains the id of the Product to update, as well as its new properties
   */
  void updateProduct(Product newProduct);

  void deleteProduct(int id);

}
