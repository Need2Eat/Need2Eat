package at.need2eat.need2eat.database;

import android.content.Context;

import at.need2eat.need2eat.Product;

/**
 * This class is used to access the local database asynchronously and either insert, delete or
 * update a {@link Product}
 * @author Maxi Nothnagel - mx.nothnagel@gmail.com
 */
public class DatabaseTask implements Runnable {

  private Product product;
  private DatabaseHandler handler;
  private boolean delete;

  /**
   * Creates a new DatabaseTask {@code Thread}
   * @param product the {@code Product} you want to update, delete or insert in the database
   * @param context the {@code Context} to use, create or open the database
   * @param delete {@code true} if the given {@code Product} should be deleted from the database
   */
  public DatabaseTask(Product product, Context context, boolean delete) {
    this.product = product;
    handler = new DatabaseHandler(context);
    this.delete = delete;
  }

  @Override
  public void run() {
    if (delete) {
      handler.deleteProduct(product.getID());
    } else {
      if (product.getID() == 0) {
        handler.addProduct(new Product(product.getGTIN(), product.getName(),
            product.getExpiryDate()));
      } else {
        handler.updateProduct(product);
      }
    }
    handler.close();
  }
}
