package at.need2eat.need2eat.database;

import java.util.List;

import at.need2eat.need2eat.Product;

/**
 * Created by AM307 on 09.12.2015.
 */
public interface DatabaseManager {

  /**
   * This method should return all {@link Product}s from the database at once.
   * @param sorted {@code true} if the resulted {@code List} should be sorted by expiry date,
   * otherwise set {@code false}
   * @return a {@link List} of all Products currently stored in the database
   */
  List<Product> getAllProducts(boolean sorted);

  /**
   * This method should insert the given {@link Product} into the database.
   * @param newProduct the Product to be inserted
   */
  void addProduct(Product newProduct);

  /**
   * This method should update the information of the given {@link Product} in the database with the
   * data stored in it.
   * @param newProduct the new Product data to replace existing values
   */
  void updateProduct(Product newProduct);

  /**
   * This method should delete the {@link Product} with the specified ID.
   * @param id the unique ID of the Product to delete
   */
  void deleteProduct(int id);

}
