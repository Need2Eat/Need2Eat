package at.need2eat.need2eat;

import android.support.v7.app.AppCompatActivity;

import java.util.List;

/**
 * This class represents an {@link AppCompatActivity} with extended functionality. The contents of
 * Activities that inherit from this class can be reloaded by calling {@link #refreshAdapter(List)}.
 * Type T represents the data type of the values that are needed to perform this action.
 * @author AM307
 */
public abstract class AdapterActivity<T> extends AppCompatActivity {

  /**
   * Overriding methods should implement behaviour that reloads the contents of the inheriting
   * Activity using the list of items given.
   * @param items the list of items that should be used to reload the Activity's contents
   */
  public abstract void refreshAdapter(List<T> items);

}
