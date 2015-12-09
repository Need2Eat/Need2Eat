package at.need2eat.need2eat;


import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

/**
 * This is a utility class providing simple error logging functionality
 *
 * @author Maxi Nothnagel - mx.nothnagel@gmail.com
 */
public class LogUtils {

  /**
   * Create a new {@code AlertDialog} that contains the error-log message and log the
   * {@code Exception}
   * @param context The {@link Context} the {@code View} and the {@code AlertDialog} are running in
   * @param tag Used to identify the source of the log message. It usually identifies the class or
   * activity where the log occurs
   * @param msg The message you would like to log
   * @param ex The {@code Exception} that should be logged
   * @return The {@link AlertDialog} object that was used for logging the error
   */
  public static AlertDialog logError(Context context, String tag, String msg, Exception ex) {
    AlertDialog dialog = new AlertDialog.Builder(context).create();
    dialog.setTitle("Fehler!");
    dialog.setMessage(msg);
    dialog.show();
    Log.e(tag, ex.getMessage(), ex);

    return dialog;
  }

}
