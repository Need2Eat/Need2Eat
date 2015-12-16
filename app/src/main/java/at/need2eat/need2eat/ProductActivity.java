package at.need2eat.need2eat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

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

  private void setTextviewProductName(String productName){
    textviewProductName.setText(productName);
  }

  private void setTextviewGTIN(String gtin){
    textviewGTIN.setText(gtin);
  }

  private void setTextviewAblauf(Date expiryDate){
    //textviewAblauf.setText(expiryDate);

  }
}
