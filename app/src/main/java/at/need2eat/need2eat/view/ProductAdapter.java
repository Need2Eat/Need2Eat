package at.need2eat.need2eat.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import at.need2eat.need2eat.Product;
import at.need2eat.need2eat.R;

/**
 * Created by AM307 on 05.12.2015.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {

  private List<Product> products;
  private ProductClickListener clickListener;

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
