package at.need2eat.need2eat;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.*;

import java.text.ParseException;
import java.util.Date;

import at.need2eat.need2eat.database.DatabaseTask;
import at.need2eat.need2eat.database.OutpanManager;
import at.need2eat.need2eat.util.DateConverter;
import at.need2eat.need2eat.util.LogUtils;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * This class contains the interface of the EditActivity, which allows the user to edit
 * information of a product manually.
 * @author Sebastian Feistl
 * @author Maxi Nothnagel - mx.nothnagel@gmail.com
 */
public class EditActivity extends AppCompatActivity {

  /**
   * This private class starts an asynchronous task to connect to the Outpan database and get the
   * name of a specific product. To start the asynchronous task call
   * {@link at.need2eat.need2eat.EditActivity.OutpanTask#execute(Object[])} with a {@code String}
   * Array containing the API key of Outpan (first element) and the GTIN of the product. The
   * {@code EditText} field containing the name of the product will be set automatically after the
   * call has been finished
   */
  private class OutpanTask extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... params) {
      productname = new OutpanManager(params[0]).getName(params[1]);
      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      productnameEdit.setText(productname);
    }
  }

  @Bind(R.id.productNameEdit) protected EditText productnameEdit;
  @Bind(R.id.gtinTextEdit) protected EditText gtinEdit;
  @Bind(R.id.dateTextEdit) protected EditText dateEdit;

  @Bind(R.id.backButton) protected LinearLayout backButton;
  @Bind(R.id.acceptButton) protected LinearLayout acceptButton;

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

    ButterKnife.bind(this);

    Resources resources = getResources();

    Intent intent = getIntent();
    Bundle bundle = intent.getExtras();

    if (bundle != null) {
      Product product = (Product) bundle.get(resources.getString(R.string.extra_product));
      id = product.getID();
      date = product.getExpiryDate();
      productname = product.getName();
      gtin = product.getGTIN();

      if (productname == null) {
        new OutpanTask().execute(resources.getString(R.string.api_key), gtin);
      }
    }

    initializeLayout();
  }

  /**
   * Function to initialize all important elements and applies onClickListeners to the buttons.
   * Furthermore it sets the current existing information (name of the product, barcode,
   * expiration date).
   */
  private void initializeLayout() {
    productnameEdit.setText(productname);
    gtinEdit.setText(gtin);
    try {
      dateEdit.setText(DateConverter.getStringFromDate(date));
    } catch (IllegalArgumentException e) {
      dateEdit.setText("");
    }
  }

  @OnClick(R.id.acceptButton)
  public void onAcceptButtonClicked() {
    productname = productnameEdit.getText().toString();
    gtin = gtinEdit.getText().toString();
    try {
      date = DateConverter.getDateFromString(dateEdit.getText().toString());
    } catch (ParseException e) {
      date = null;
    }

    if (productname.equals("") || date == null) {
      LogUtils.logInformation(EditActivity.this, EditActivity.class.getSimpleName(), "Achtung!",
          "Bitte alle Felder ausfüllen!");
    } else {
      new DatabaseTask(new Product(id, gtin, productname, date), this, false).run();
      finish();
    }
  }

  @OnClick(R.id.backButton)
  public void onBackButtonClicked() {
    finish();
  }

}
