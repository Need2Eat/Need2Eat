package at.need2eat.need2eat.barcode.reader;

import android.app.AlertDialog;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.IOException;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

  private SurfaceHolder holder;
  private Camera camera;
  private static final String TAG = "Camera";
  private int width;
  private int height;
  private MultiFormatReader reader;
  private int left;
  private int top;
  private int areaWidth;
  private int areaHeight;
  private AlertDialog dialog;

  public CameraPreview(Context context, Camera camera) {
    super(context);
    this.camera = camera;
    holder = getHolder();
    holder.addCallback(this);

    Parameters params = camera.getParameters();

    width = 640;
    height = 480;

    params.setPreviewSize(width, height);
    camera.setParameters(params);

    reader = new MultiFormatReader();
    dialog = new AlertDialog.Builder(context).create();
  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {
    try {
      camera.setPreviewDisplay(holder);
      camera.startPreview();
    } catch (IOException e) {
      dialog.setTitle("Fehler!");
      dialog.setMessage("Surface zur Vorschau-Anzeige nicht verfügbar!");
      dialog.show();
      Log.e(TAG, e.getMessage(), e);
    }
  }

  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    if (this.holder.getSurface() != null) {
      try {
        camera.stopPreview();
        camera.setPreviewCallback(callback);
        camera.setPreviewDisplay(holder);
        camera.startPreview();
      } catch (IOException e) {
        dialog.setTitle("Fehler!");
        dialog.setMessage("Surface zur Vorschau-Anzeige nicht verfügbar!");
        dialog.show();
        Log.e(TAG, e.getMessage(), e);
      }

    }
  }

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {

  }

  public void setCamera(Camera camera) {
    this.camera = camera;
  }

  public void onPause() {
    if (camera != null) {
      camera.setPreviewCallback(null);
      camera.stopPreview();
    }
  }

  public void setArea(int left, int top, int width) {
    double ratio = width / this.width;
    this.left = (int) (left / (ratio + 1));
    this.top = (int)  (top / (ratio + 1));
    this.areaWidth = this.width - this.left * 2;
    this.areaHeight = this.width - this.left * 2;
  }

  private PreviewCallback callback = new PreviewCallback() {
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
      if (dialog.isShowing()) {
        return;
      }

      LuminanceSource source = new PlanarYUVLuminanceSource(data, width, height, left, top, areaWidth, areaHeight, false);
      BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
      Result result;

      try {
        result = reader.decode(bitmap, null);
        if (result != null) {
          /*
            Insert code that handles the result of the barcode scanner
           */
        }
      } catch (NotFoundException e) {
        dialog.setTitle("Keinen Barcode gefunden!");
        dialog.setMessage("Stellen Sie sicher, dass der Barcode nicht abgedeckt ist!");
        dialog.show();
        Log.e(TAG, e.getMessage(), e);
      }
    }
  };
}
