package at.need2eat.need2eat.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import at.need2eat.need2eat.Product;
import at.need2eat.need2eat.R;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * This class represents a slot in a RecyclerView with an {@link IndicatorView} and two
 * {@link TextView}s.
 * @author AM307
 */
public class ProductViewHolder extends RecyclerView.ViewHolder {

  //Views declared in the XML layout file for a Product RecyclerView item
  @Bind(R.id.productName) protected TextView productNameView;
  @Bind(R.id.expirationDate) protected TextView expirationDateView;
  @Bind(R.id.indicator) protected IndicatorView indicatorView;

  /* The product this ViewHolder is holding */
  private Product product;

  /**
   * Initializes a new ProductViewHolder with the given {@link View}.
   * The {@link ProductClickListener} is called when the item this ViewHolder is holding is being
   * clicked on in the GUI.
   * @param itemView the View containing the visual representation of this ViewHolder's Product
   * @param clickListener a listener that will be notified when a click on a
   * {@link ProductViewHolder} occurs
   */
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

  /**
   * Bind this ViewHolder to the values of the given {@link Product}.
   * @param product the Product whose data should be displayed in this ViewHolder
   */
  protected void setProduct(Product product) {
    this.product = product;
    indicatorView.update(product.getDaysUntilExpiry());
    productNameView.setText(product.getName());
    String expiryDate = product.getExpiryDateString();
    expirationDateView.setText(expiryDate);
  }

}
