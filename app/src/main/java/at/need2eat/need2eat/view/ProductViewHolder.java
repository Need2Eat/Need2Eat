package at.need2eat.need2eat.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
  @Bind(R.id.indicator) protected IndicatorView indicatorView;

  private static final Locale EXPIRY_DATE_LOCALE = Locale.GERMANY;
  private static final String EXPIRY_DATE_FORMAT = "dd.mm.yyyy";

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
    indicatorView.update(3);
    productNameView.setText(product.getName());
    String expiryDate = getDateStringLocale(product.getExpiryDate());
    expirationDateView.setText(expiryDate);
  }

  private String getDateStringLocale(Date date) {
    SimpleDateFormat dateFormat = new SimpleDateFormat(EXPIRY_DATE_FORMAT, EXPIRY_DATE_LOCALE);
    return dateFormat.format(date);
  }

}
