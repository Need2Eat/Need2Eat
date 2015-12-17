package at.need2eat.need2eat;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import at.need2eat.need2eat.database.DatabaseHandler;
import at.need2eat.need2eat.database.OutpanManager;
import at.need2eat.need2eat.util.LogUtils;

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
  private Date date;

  /** The ID of the product in the local database */
  private int id;

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

    if (bundle != null) {
      try {
        id = bundle.getInt(resources.getString(R.string.extra_id));
      } catch (Resources.NotFoundException e) {
        id = 0;
      }

      if (bundle.getString(NAME) == null) {
        bundle.putString(NAME,
            new OutpanManager(resources.getString(R.string.api_key))
                .getName(GTIN));
      }

      if (bundle.getString(EXPIRY_DATE) == null) {
        date = null;
      } else {
        try {
          date = getDateFromString(bundle.getString(EXPIRY_DATE));
        } catch (ParseException e) {
          date = null;
        }
      }

      setData(bundle.getString(NAME), bundle.getString(GTIN), date);
    } else {
      setData("", "", null);
    }

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
        try {
          date = getDateFromString(DATE_EDIT.getText().toString());
        } catch (ParseException e) {
          date = null;
        }

        if (productname.equals("") || gtin.equals("") || date == null) {
          LogUtils.logInformation(EditActivity.this, EditActivity.class.getSimpleName(), "Achtung!",
              "Bitte alle Felder ausfüllen!");
        } else {
          DatabaseHandler handler = new DatabaseHandler(EditActivity.this);
          if (id == 0) {
            handler.addProduct(new Product(gtin, productname, date));
            finish();
          } else {
            handler.updateProduct(new Product(id, gtin, productname, date));
            finish();
          }
        }
      }
    });

    PRODUCTNAME_EDIT.setText(productname);
    GTIN_EDIT.setText(gtin);
    try {
      DATE_EDIT.setText(getStringFromDate(date));
    } catch (IllegalArgumentException e) {
      DATE_EDIT.setText("");
    }
  }

  /**
   * Function to set or update the information of the product.
   * @param productname is the full name of the current product.
   * @param gtin is the barcode of the product, saved as digits.
   * @param date is the expiration date of the product.
   */
  private void setData(String productname, String gtin, Date date) {
    this.productname = (productname == null) ? "" : productname;
    this.gtin = gtin;
    this.date = (date == null) ? null : date;
  }

  private Date getDateFromString(String expiryDate) throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("dd.mm.yyyy", Locale.GERMANY);
    return sdf.parse(expiryDate);
  }

  private String getStringFromDate(Date expiryDate) throws IllegalArgumentException {
    if (expiryDate == null) {
      throw new IllegalArgumentException();
    }
    SimpleDateFormat sdf = new SimpleDateFormat("dd.mm.yyyy", Locale.GERMANY);
    return sdf.format(expiryDate);
  }

}
