package at.need2eat.need2eat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * This class contains the User Interface for showing the product information for a specific
 * product.
 * @author Mario Kriszta
 */
public class ProductActivity extends AppCompatActivity {

  @Bind(R.id.TextviewName) protected  TextView textviewProductName;
  @Bind(R.id.TextviewGTIN) protected TextView textviewGTIN;
  @Bind(R.id.TextviewAblauf) protected TextView textviewAblauf;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_product);
    ButterKnife.bind(this);
  }

  /**
   * Function sets the name label of the product to the product name.
   * @param productName is the name of the product as String.
   */
  private void setTextviewProductName(String productName){
    textviewProductName.setText(productName);
  }

  /**
   * Function sets the GTIN label to the barcode of the product.
   * @param gtin is the barcode of the product as String.
   */
  private void setTextviewGTIN(String gtin){
    textviewGTIN.setText(gtin);
  }

  /**
   * Function sets the expiry date label to the expiry date of the product.
   * @param expiryDate of the product as java.util.Date
   */
  private void setTextviewAblauf(Date expiryDate){
    SimpleDateFormat sdf = new SimpleDateFormat("dd.mm.yyyy", Locale.GERMANY);
    textviewAblauf.setText(sdf.format(expiryDate));

  }
}
