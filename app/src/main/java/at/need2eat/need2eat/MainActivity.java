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

import java.util.ArrayList;
import java.util.List;

import at.need2eat.need2eat.view.ProductAdapter;
import at.need2eat.need2eat.view.ProductClickListener;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

  /**
   * The Toolbar used by the Android Support Library to implement the {@link android.app.ActionBar}
   */
  @Bind(R.id.toolbar) protected Toolbar toolbar;
  @Bind(R.id.productView) protected RecyclerView productView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    setSupportActionBar(toolbar);
    List<Product> products = new ArrayList<>();
    products.add(new Product("Milka Haselnuss Schokolade", "12/22/2015"));
    for (int i = 0; i < 10; i++) {
      products.add(new Product("Tolles neues Produkt", "99/99/2075"));
    }
    products.add(new Product("Test", "89/99/2075"));
    productView.setAdapter(new ProductAdapter(products, new ProductClickListener() {
      @Override
      public void onItemClick(Product product) {
        System.out.println(product);
      }
    }));
    productView.setLayoutManager(new LinearLayoutManager(this));
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
      case R.id.action_add:
        Intent intent = new Intent(this, EditActivity.class);
        String extraKey = getResources().getString(R.string.extra_origin);
        intent.putExtra(extraKey, MainActivity.class);
        startActivity(intent);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

}
