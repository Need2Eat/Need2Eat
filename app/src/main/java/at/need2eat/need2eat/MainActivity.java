package at.need2eat.need2eat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.List;

import at.need2eat.need2eat.barcode.reader.ScannerActivity;
import at.need2eat.need2eat.database.DatabaseMode;
import at.need2eat.need2eat.database.DatabaseTask;
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
public class MainActivity extends AdapterActivity<Product> {

  private MainClickListener clickListener = new MainClickListener();

  /**
   * Used to determine whether the currently selected items are sorted or not.
   * {@link DatabaseMode#SELECT} indicates an unsorted, {@link DatabaseMode#SORTED_SELECT} a sorted
   * list of products.
   */
  private DatabaseMode mode = DatabaseMode.SELECT;

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

    productView.setLayoutManager(new LinearLayoutManager(this));
  }

  /**
   * Starts a new {@link DatabaseTask} that will call {@link #refreshAdapter(List)} with the
   * current entries from the database once finished.
   */
  private void loadProductsAsync() {
    new DatabaseTask(this, mode).run();
  }

  @Override
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
      //Button to sort products by expiry date
      case R.id.action_sort:
        mode = (mode == DatabaseMode.SELECT) ? DatabaseMode.SORTED_SELECT : DatabaseMode.SELECT;
        loadProductsAsync();
        // FALL-THROUGH
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
    loadProductsAsync();
    super.onResume();
  }

  private class MainClickListener implements ProductClickListener {

    @Override
    public void onItemClick(Product product) {
      //when an item is clicked, we take the user to the ProductActivity
      Intent intent = new Intent(MainActivity.this, ProductActivity.class);
      intent.putExtra(getResources().getString(R.string.extra_product), product);
      startActivity(intent);
    }

  }

}
