package at.need2eat.need2eat.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import at.need2eat.need2eat.Product;
import at.need2eat.need2eat.R;

/**
 * This class represents a link between a data source containing {@link Product}s and a
 * {@link RecyclerView} in the GUI.
 * @author AM307
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {

  //List of all currently displayed Products
  private List<Product> products;

  //Listener for clicks on ProductViewHolders in the GUI
  private ProductClickListener clickListener;

  /**
   * Initializes a new ProductAdapter with a given {@link List} of {@link Product}s and a
   * {@link ProductClickListener}.
   * @param products a list of products that are to be initially displayed
   * @param clickListener a listener that will be notified when a click on a
   * {@link ProductViewHolder} occurs.
   */
  public ProductAdapter(List<Product> products, ProductClickListener clickListener) {
    this.products = products;
    this.clickListener = clickListener;
  }

  @Override
  public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View productView = inflater.inflate(R.layout.item_product, parent, false);
    return new ProductViewHolder(productView, clickListener);
  }

  @Override
  public void onBindViewHolder(ProductViewHolder holder, int position) {
    holder.setProduct(products.get(position));
  }

  @Override
  public int getItemCount() {
    return products.size();
  }

}
