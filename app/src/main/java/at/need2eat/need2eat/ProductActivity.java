package at.need2eat.need2eat;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Date;

import at.need2eat.need2eat.database.DatabaseTask;
import at.need2eat.need2eat.util.DateConverter;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * This class contains the User Interface for showing the product information for a specific
 * product.
 * @author Mario Kriszta
 * @author Maxi Nothnagel - mx.nothnagel@gmail.com
 */
public class ProductActivity extends AppCompatActivity {

  @Bind(R.id.TextviewName) protected  TextView textviewProductName;
  @Bind(R.id.TextviewGTIN) protected TextView textviewGTIN;
  @Bind(R.id.TextviewAblauf) protected TextView textviewAblauf;

  @Bind(R.id.backButton) protected LinearLayout BACK_BUTTON;
  @Bind(R.id.deleteButton) protected LinearLayout DELETE_BUTTON;
  @Bind(R.id.editButton) protected LinearLayout EDIT_BUTTON;

  private int id;
  private Product product;

  Resources resources;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_product);
    ButterKnife.bind(this);

    resources = getResources();

    intitializeLayout();
  }

  /**
   * Function to initialize all important elements and applies onClickListeners to the buttons.
   */
  private void intitializeLayout() {
    Intent intent = getIntent();
    Bundle bundle = intent.getExtras();

    product = (Product) bundle.get(resources.getString(R.string.extra_product));
    id = product.getID();
    setTextviewAblauf(product.getExpiryDate());
    setTextviewGTIN(product.getGTIN());
    setTextviewProductName(product.getName());
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
    textviewAblauf.setText(DateConverter.getStringFromDate(expiryDate));
  }

  @OnClick(R.id.backButton)
  public void onBackButtonClicked() {
    finish();
  }

  @OnClick(R.id.deleteButton)
  public void onDeleteButtonClicked() {
    new DatabaseTask(product, this, DatabaseTask.DatabaseMode.DELETE).run();
    finish();
  }

  @OnClick(R.id.editButton)
  public void onAcceptButtonClicked() {
    Intent intent = new Intent(ProductActivity.this, EditActivity.class);

    Date date;

    try {
      date = DateConverter.getDateFromString(textviewAblauf.getText().toString());
    } catch (ParseException e) {
      date = null;
    }

    Product product = new Product(id, textviewGTIN.getText().toString(),
        textviewProductName.getText().toString(), date);

    intent.putExtra(resources.getString(R.string.extra_product), product);

    ProductActivity.this.startActivity(intent);
    finish();
  }
}
