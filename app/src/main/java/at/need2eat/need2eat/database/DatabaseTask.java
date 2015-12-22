package at.need2eat.need2eat.database;

import android.content.Context;

import at.need2eat.need2eat.Product;

/**
 *
 * @author Maxi Nothnagel - mx.nothnagel@gmail.com
 */
public class DatabaseTask implements Runnable {

  private Product product;
  private DatabaseHandler handler;
  private boolean delete;

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
