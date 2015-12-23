package at.need2eat.need2eat.barcode.reader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.widget.FrameLayout;

import at.need2eat.need2eat.util.LogUtils;
import at.need2eat.need2eat.R;

/**
 * This class contains the interface of the {@code ScannerActivity} which is used to start the 
 * camera via a {@link CameraManager} that scans a barcode.
 * @author Maxi Nothnagel - mx.nothnagel@gmail.com
 */
public class ScannerActivity extends AppCompatActivity {

  private CameraPreview camPreview;
  private CameraManager manager;
  private HoverView view;

  /**
   * Auto-generated function that initializes the layout of this class. Furthermore, it starts
   * a {@link CameraManager} and adds a {@link CameraPreview} to the
   * {@link android.hardware.Camera} in order to scan a barcode.
   * @param savedInstanceState This {@link Bundle} contains the data it most recently supplied
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_scanner);
    Display display = getWindowManager().getDefaultDisplay();
    view = (HoverView) findViewById(R.id.hover_view);
    Point size = new Point();
    display.getSize(size);
    view.update(size.x, size.y);

    try {
      manager = new CameraManager();
      camPreview = new CameraPreview(this, manager.getCamera());
      // camPreview.setArea(view.getHoverLeft(), view.getHoverTop(), size.x);

      FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
      preview.addView(camPreview);
    }  catch (CameraNotFoundException e) {
      AlertDialog alert = LogUtils.logError(this, ScannerActivity.class.getSimpleName(),
          "Ihr Handy ist nicht mit unserem Barcode-Scanner kompatibel, da es keine Kamera besitzt!",
          e);

      alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
          finish();
        }
      });
    } catch (CameraNotAccessibleException e) {
      AlertDialog alert = LogUtils.logError(this, ScannerActivity.class.getSimpleName(),
          "Kamera ist nicht verfügbar! Eventuell andere Anwendungen schließen!", e);

      alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
          finish();
        }
      });
    }

  }

  /**
   * Starts the {@link Activity#onPause()} method. Furthermore, it pauses the
   * {@link CameraPreview} and the {@link CameraManager}
   */
  @Override
  protected void onPause() {
    try {
      camPreview.onPause();
      manager.onPause();
    } catch (RuntimeException e) {
      /*
      This catch block can be left empty as the exception will only get thrown whenever
      a camera device is not available or non-existing. In this case an alert dialog is shown
      that finishes the activity as soon as it gets dismissed. Therefor, the manager does not need
      to get paused.
       */
    }
    super.onPause();
  }

  /**
   * Starts the {@link Activity#onResume()} method. Furthermore, it resumes the
   * {@link CameraPreview} and the {@link CameraManager} and it sets the
   * {@link android.hardware.Camera} to the CameraManager
   */
  @Override
  protected void onResume() {
    super.onResume();
    try {
      manager.onResume();
      camPreview.setCamera(manager.getCamera());
    } catch (CameraNotFoundException | CameraNotAccessibleException | NullPointerException e) {
      /*
      This catch block can be left empty as the exception will only get thrown whenever
      a camera device is not available or non-existing. In this case an alert dialog is shown
      that finishes the activity as soon as it gets dismissed. Therefor, the manager does not need
      to get paused.
      */
    }
  }

  /**
   * Starts the {@link Activity#onDestroy()} method. Furthermore, it releases the camera by
   * calling {@link CameraManager#onPause()} and {@link CameraPreview#onPause()}
   */
  @Override
  protected void onDestroy() {
    try{
      camPreview.onPause();
      manager.onPause();
    } catch (RuntimeException e) {
      /*
      This catch block can be left empty as the exception will only get thrown whenever
      a camera device is not available or non-existing. In this case an alert dialog is shown
      that finishes the activity as soon as it gets dismissed. Therefor, the manager does not need
      to get paused.
      */
    }
    super.onDestroy();
  }
}
