package at.need2eat.need2eat.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import at.need2eat.need2eat.Product;
import at.need2eat.need2eat.R;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by AM307 on 05.12.2015.
 */
public class ProductViewHolder extends RecyclerView.ViewHolder {

  @Bind(R.id.productName) protected TextView productNameView;
  @Bind(R.id.expirationDate) protected TextView expirationDateView;
  private Product product;

  public ProductViewHolder(final View itemView, final ProductClickListener clickListener) {
    super(itemView);
    ButterKnife.bind(this, itemView);
    itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        clickListener.onItemClick(product);
      }
    });
  }

  protected void setProduct(Product product) {
    this.product = product;
    productNameView.setText(product.getName());
    expirationDateView.setText(product.getExpiryDate());
  }

}
