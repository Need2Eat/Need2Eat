package at.need2eat.need2eat;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
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

    intitializeLayout();
  }

  /**
   * Function to initialize all important elements and applies onClickListeners to the buttons.
   */
  private void intitializeLayout() {
    final LinearLayout BACK_BUTTON = (LinearLayout)findViewById(R.id.backButton);
    final LinearLayout DELETE_BUTTON = (LinearLayout)findViewById(R.id.deleteButton);
    final LinearLayout EDIT_BUTTON = (LinearLayout)findViewById(R.id.editButton);

    BACK_BUTTON.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    DELETE_BUTTON.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // TODO: delete product from database
        finish();
      }
    });

    EDIT_BUTTON.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Resources resources = getResources();
        final String GTIN = resources.getString(R.string.extra_gtin);
        final String EXPIRY_DATE = resources.getString(R.string.extra_expiry);
        final String NAME = resources.getString(R.string.extra_name);
        Intent intent = new Intent(ProductActivity.this, EditActivity.class);
        intent.putExtra(GTIN, "48495384");
        intent.putExtra(EXPIRY_DATE, "12.12.2017");
        intent.putExtra(NAME, "HansHandcreme");
        intent.putExtra(resources.getString(R.string.extra_id), 112);
        ProductActivity.this.startActivity(intent);
        finish();
      }
    });

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
