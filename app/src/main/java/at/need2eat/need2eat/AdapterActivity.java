package at.need2eat.need2eat;

import android.support.v7.app.AppCompatActivity;

import java.util.List;

/**
 * Created by AM307 on 22.12.2015.
 */
public abstract class AdapterActivity<T> extends AppCompatActivity {

  public abstract void refreshAdapter(List<T> items);

}
