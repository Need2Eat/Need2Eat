package at.need2eat.need2eat.database;

import android.content.Context;

import at.need2eat.need2eat.AdapterActivity;
import at.need2eat.need2eat.Product;

/**
 * This class is used to access the local database asynchronously and either insert, delete or
 * update a {@link Product}
 * @author Maxi Nothnagel - mx.nothnagel@gmail.com
 */
public class DatabaseTask implements Runnable {

  private Product product;
  private DatabaseHandler handler;
  private AdapterActivity<Product> activity;
  private DatabaseMode selectedMode;

  /**
   * Creates a new DatabaseTask {@code Thread} for the SQL statements:
   * <ul>
   *   <li>{@code insert}</li>
   *   <li>{@code update}</li>
   *   <li>{@code delete}</li>
   * </ul>
   * using the {@code Product} given
   * @param product the {@code Product} you want to update, delete or insert in the database
   * @param context the {@code Context} to use, create or open the database
   * @param selectedMode the SQL statement you want to execute (either {@link DatabaseMode#INSERT},
   * {@link DatabaseMode#UPDATE} or {@link DatabaseMode#DELETE}
   */
  public DatabaseTask(Product product, Context context, DatabaseMode selectedMode) {
    this.product = product;
    handler = new DatabaseHandler(context);
    this.selectedMode = selectedMode;
  }

  /**
   * Creates a new DatabaseTask {@code Thread} for the {@code select} SQL statement
   * @param activity the {@code Activity} the result of the {@code select} should be send to
   * @param selectedMode the SQL statement you want to execute (either {@link DatabaseMode#SELECT},
   * or {@link DatabaseMode#SORTED_SELECT}
   */
  public DatabaseTask(AdapterActivity<Product> activity, DatabaseMode selectedMode) {
    handler = new DatabaseHandler(activity);
    this.activity = activity;
    this.selectedMode = selectedMode;
  }

  @Override
  public void run() {
    switch (selectedMode) {
      case SELECT:
        activity.refreshAdapter(handler.getAllProducts(false));
        break;
      case SORTED_SELECT:
        activity.refreshAdapter(handler.getAllProducts(true));
        break;
      case DELETE:
        handler.deleteProduct(product.getID());
        break;
      case INSERT:
        handler.addProduct(product);
        break;
      case UPDATE:
        handler.updateProduct(product);
        break;
      default:
        /*
        This default block should not be reachable. However, if it is reached, a
        IllegalArgumentException gets thrown
         */
        throw new IllegalArgumentException();
    }
    handler.close();
  }
}
