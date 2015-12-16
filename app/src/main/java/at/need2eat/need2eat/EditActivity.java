package at.need2eat.need2eat;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

import java.util.Date;

import at.need2eat.need2eat.database.DatabaseHandler;
import at.need2eat.need2eat.database.OutpanManager;

/**
 * This class contains the interface of the EditActivity, which allows the user to edit
 * information of a product manually.
 * @author Sebastian Feistl
 * @author Maxi Nothnagel - mx.nothnagel@gmail.com
 */
public class EditActivity extends AppCompatActivity {

  /** The full name of the particular product. */
  private String productname;

  /** The barcode saved as digits of the particular product. */
  private String gtin;

  /** The expiration date of the particular product. */
  private String date;

  /** The ID of the product in the local database */
  private int id;

  /**
   * Autogenerated function that initializes the layout of this class.
   * @param savedInstanceState TODO: Look up what the heck that is
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit);

    Resources resources = getResources();

    Intent intent = getIntent();
    Bundle bundle = intent.getExtras();

    final String GTIN = resources.getString(R.string.extra_gtin);
    final String EXPIRY_DATE = resources.getString(R.string.extra_expiry);
    final String NAME = resources.getString(R.string.extra_name);

    try {
      id = Integer.parseInt(resources.getString(R.string.extra_id));
    } catch (Resources.NotFoundException e) {
      id = -1;
    }

    if (bundle.get(NAME) == null) {
      bundle.putString(NAME,
          new OutpanManager(resources.getString(R.string.api_key))
              .getName(GTIN));
    }

    setData(bundle.getString(NAME), bundle.getString(GTIN), bundle.getString(EXPIRY_DATE));

    // setData("Tutti Frutti", "0184977832175", "10.12.2017");
    initializeLayout();
  }

  /**
   * Function to initialize all important elements and applies onClickListeners to the buttons.
   * Furthermore it sets the current existing information (name of the product, barcode,
   * expiration date).
   */
  private void initializeLayout() {

    final EditText PRODUCTNAME_EDIT = (EditText)findViewById(R.id.productNameEdit);
    final EditText GTIN_EDIT = (EditText)findViewById(R.id.gtinTextEdit);
    final EditText DATE_EDIT = (EditText)findViewById(R.id.dateTextEdit);
    final LinearLayout BACK_BUTTON = (LinearLayout)findViewById(R.id.backButton);
    final LinearLayout ACCEPT_BUTTON = (LinearLayout)findViewById(R.id.acceptButton);

    BACK_BUTTON.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    ACCEPT_BUTTON.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        productname = PRODUCTNAME_EDIT.getText().toString();
        gtin = GTIN_EDIT.getText().toString();
        date = DATE_EDIT.getText().toString();

        if (productname == null || gtin == null || date == null) {
          LogUtils.logInformation(EditActivity.this, EditActivity.class.getSimpleName(),
              "Achtung!", "Bitte alle Felder ausfüllen!");
        } else {
          DatabaseHandler handler = new DatabaseHandler(EditActivity.this);
          if (id == -1) {
            // handler.addProduct(new Product(gtin, productname, date));
          }
        }
      }
    });

    PRODUCTNAME_EDIT.setText(productname);
    GTIN_EDIT.setText(gtin);
    DATE_EDIT.setText(date);
  }

  /**
   * Function to set or update the information of the product.
   * @param productname is the full name of the current product.
   * @param gtin is the barcode of the product, saved as digits.
   * @param date is the expiration date of the product.
   */
  private void setData(String productname, String gtin, String date) {
    this.productname = (productname == null) ? "" : productname;
    this.gtin = gtin;
    this.date = (date == null) ? "" : date;
  }

}
