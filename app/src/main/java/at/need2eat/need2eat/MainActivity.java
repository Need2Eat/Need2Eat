package at.need2eat.need2eat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.List;

import at.need2eat.need2eat.barcode.reader.ScannerActivity;
import at.need2eat.need2eat.database.DatabaseHandler;
import at.need2eat.need2eat.view.ProductAdapter;
import at.need2eat.need2eat.view.ProductClickListener;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * This class represents the first Activity that is started after the user opened up the app.
 * It provides the functionality for the various elements on the GUI such as the + button or
 * the {@link android.support.design.widget.FloatingActionButton}.
 * @author AM307
 */
public class MainActivity extends AppCompatActivity {

  private class MainClickListener implements ProductClickListener {

    @Override
    public void onItemClick(Product product) {
      Intent intent = new Intent(MainActivity.this, ProductActivity.class);
      intent.putExtra(getResources().getString(R.string.extra_product), product);
      startActivity(intent);
    }

  }

  private final DatabaseHandler dbHandler = new DatabaseHandler(this);

  private MainClickListener clickListener = new MainClickListener();

  /**
   * The Toolbar used by the Android Support Library to implement an {@link android.app.ActionBar}.
   * This element is automatically injected by ButterKnife in the {@link #onCreate(Bundle)} method.
   */
  @Bind(R.id.toolbar) protected Toolbar toolbar;

  /**
   * The {@link RecyclerView} responsible for displaying a list of {@link Product}s.
   * This element is automatically injected by ButterKnife in the {@link #onCreate(Bundle)} method.
   */
  @Bind(R.id.productView) protected RecyclerView productView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    //Bind Views defined in the XML layout file to local variables
    ButterKnife.bind(this);

    //Provide the Toolbar injected by ButterKnife as a substitute for the ActionBar
    setSupportActionBar(toolbar);

    //Add example Products to the RecyclerView
    /*List<Product> products = new ArrayList<>();
    products.add(new Product("Milka Haselnuss Schokolade", new Date(115, 11, 22)));
    products.add(new Product("Milka Haselnuss Schokolade", new Date(115, 11, 31)));
    for (int i = 0; i < 10; i++) {
      products.add(new Product("Tolles neues Produkt", new Date(175, 0, 4)));
    }
    products.add(new Product("Test", new Date(176, 5, 25)));*/

    clickListener = new MainClickListener();

    List<Product> products = dbHandler.getAllProducts(false);

    //Add an Adapter to the RecyclerView, which will bind data from our internal database to the GUI
    refreshAdapter(products);
    productView.setLayoutManager(new LinearLayoutManager(this));
  }

  public void refreshAdapter(List<Product> products) {
    productView.setAdapter(new ProductAdapter(products, clickListener));
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.action_buttons, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      //Button to manually add a product
      case R.id.action_add:
        Intent intent = new Intent(this, EditActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
        return true;
      //Under normal circumstances, this block should never be reached
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @OnClick(R.id.fab)
  public void onScanButtonClicked() {
    Intent intent = new Intent(this, ScannerActivity.class);
    startActivity(intent);
    overridePendingTransition(R.anim.fab_in, R.anim.hold);
  }

  @Override
  protected void onResume() {
    refreshAdapter(dbHandler.getAllProducts(false));
    super.onResume();
  }
}
