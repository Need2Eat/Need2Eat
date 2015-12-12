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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Locale;

import at.need2eat.need2eat.view.ProductAdapter;
import at.need2eat.need2eat.view.ProductClickListener;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * This class represents the first Activity that is started after the user opened up the app.
 * It provides the functionality for the various elements on the GUI such as the + button or
 * the {@link android.support.design.widget.FloatingActionButton}.
 * @author AM307
 */
public class MainActivity extends AppCompatActivity {

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
    List<Product> products = new ArrayList<>();
    //products.add(new Product("Milka Haselnuss Schokolade", "12/22/2015"));    // original code
    products.add(new Product("Milka Haselnuss Schokolade", createDateFromString("12.22.2015")));
    for (int i = 0; i < 10; i++) {
      //products.add(new Product("Tolles neues Produkt", "99/99/2075"));        // original code
      products.add(new Product("Tolles neues Produkt", createDateFromString("99.99.2075")));
    }
    //products.add(new Product("Test", "89/99/2075"));                          // original code
    products.add(new Product("Test", createDateFromString("89.99.2075")));

    //Add an Adapter to the RecyclerView, which will bind data from our internal database to the GUI
    productView.setAdapter(new ProductAdapter(products, new ProductClickListener() {
      @Override
      public void onItemClick(Product product) {
        System.out.println(product);
      }
    }));
    productView.setLayoutManager(new LinearLayoutManager(this));
  }

  /**
   * Converts a String to a date and throws an exception if the date is invalid.
   *
   * @param date is the expiry date as String which will be converted
   * @return the date as java.util.Date
   */
  private Date createDateFromString(String date) {
    Date d = null;
    SimpleDateFormat dateformat = new SimpleDateFormat("dd.mm.yyyy", Locale.GERMANY);
    try {
      d = dateformat.parse(date);
    }
    catch (ParseException e){
      // TODO: What to do when the date is invalid?
    }
    return d;
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
        String extraKey = getResources().getString(R.string.extra_origin);
        intent.putExtra(extraKey, MainActivity.class);
        startActivity(intent);
        return true;
      //Under normal circumstances, this block should never be reached
      default:
        return super.onOptionsItemSelected(item);
    }
  }

}
