package at.need2eat.need2eat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

/**
 * Created by Sebastian on 25.11.2015.
 */
public class EditActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit);
    initializeListener();
  }

  private void initializeListener() {
    Button b = (Button)findViewById(R.id.button01);
    b.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        setText();
      }
    });
  }

  private void setText() {
    EditText content = (EditText)findViewById(R.id.editText01);
    TextView text = (TextView)findViewById(R.id.textView01);
    text.setText(content.getText());
  }


}
