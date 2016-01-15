package at.need2eat.need2eat.barcode.reader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import at.need2eat.need2eat.EditActivity;
import at.need2eat.need2eat.Product;
import at.need2eat.need2eat.util.DateConverter;
import at.need2eat.need2eat.util.LogUtils;
import at.need2eat.need2eat.R;

/**
 * This class creates a new {@link SurfaceView} which is used to draw the preview frames of the
 * image captures by the {@link Camera}. These preview frames are converted to a
 * {@link BinaryBitmap} and decoded by a {@link MultiFormatReader}. The decoded {@link Result} is
 * analysed whether it is a GS1-128 code and split into its AIs ("Application Identifiers"). If it
 * is a GS1, it contains further information we extract from it. However, no matter what barcode it
 * is, the information is sent to the {@link EditActivity} to insert a new product.
 * @author Maxi Nothnagel - mx.nothnagel@gmail.com
 */
class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

  private class CameraCallback implements PreviewCallback {

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
      if (dialog != null && dialog.isShowing()) {
        return;
      }

      byte[] rotatedData = new byte[data.length];
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          rotatedData[x * height + height - y - 1] = data[x + y * width];
        }
      }

      LuminanceSource source =
          new PlanarYUVLuminanceSource(rotatedData, height, width, 0, 0,
              height, width, false);
      BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
      Result result;

      try {
        Map<DecodeHintType, Object> hints = new HashMap<>();
        hints.put(DecodeHintType.ASSUME_GS1, Boolean.TRUE);
        result = reader.decode(bitmap, hints);

        if (result != null) {
          Resources resources = getResources();
          Intent intent = new Intent(context, EditActivity.class);
          String id;

          if (result.getText().contains("]C1") || result.getText().contains("\u001D")) {
            // a GS1-128 barcode containing the additional information we want
            Map<String, String> values = BarcodeAi.findAiValues(result.getText());
            id = values.get("01");
            String dateKey = (values.containsKey("17")) ? values.get("17") : values.get("15");

            try {
              Date expiryDate = DateConverter.getDateFromString(values.get(dateKey));
              intent.putExtra(resources.getString(R.string.extra_product),
                  new Product(id, null, expiryDate));
            } catch (ParseException e) {
              intent.putExtra(resources.getString(R.string.extra_product), new Product(id));
            }
          } else {
            // any other barcode, thus we just take the plain text as its GTIN
            id = result.getText();
            intent.putExtra(resources.getString(R.string.extra_product), new Product(id));
          }

          ((Activity) context).finish();
          context.startActivity(intent);
        }

      } catch (NotFoundException e) {
        /*
         This catch block can be left empty as the reader constantly tries to decode the preview
         until a decode-able barcode will be found. Therefor, the exception is caught in order to
         keep the app running.
          */
      }
    }
  }

  // These values represent measurements of the display
  private int width = 640;
  private int height = 480;

  // The ZXing reader and decoder for barcodes
  private MultiFormatReader reader;

  // These values represent the context the preview and the dialog are running in
  private Context context;
  private AlertDialog dialog;

  // These values represent the camera and its surface holder
  private Camera camera;
  private SurfaceHolder holder;

  /**
   * Creates a new Camerapreview with the {@link Context} and {@link Camera} given. Furthermore,
   * it creates a {@code SurfaceHolder} and adds a {@link android.view.SurfaceHolder.Callback} to
   * it. Finally, a {@link MultiFormatReader} is created
   * @param context The {@code Context} the {@code SurfaceView} is running in
   * @param camera The {@code Camera} which is used
   */
  public CameraPreview(Context context, Camera camera) {
    super(context);
    this.context = getContext();
    this.camera = camera;
    holder = getHolder();
    holder.addCallback(this);

    Parameters params = camera.getParameters();

    params.setPreviewSize(width, height);
    params.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
    camera.setParameters(params);
    camera.setDisplayOrientation(90);

    reader = new MultiFormatReader();
  }

  /**
   * This is called immediately after the surface is first created. A preview display for the
   * {@link Camera} is set using the {@link SurfaceHolder}. Finally, the preview is started.
   * @param holder The {@code SurfaceHolder} whose surface is being created.
   */
  @Override
  public void surfaceCreated(SurfaceHolder holder) {
    try {
      camera.setPreviewDisplay(holder);
      camera.startPreview();
    } catch (IOException e) {
      dialog = LogUtils.logError(getContext(), getClass().getSimpleName(),
          getResources().getString(R.string.message_surface), e);

      dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
          ((Activity) CameraPreview.this.context).finish();
        }
      });
    }
  }

  /**
   * This is called immediately after any structural changes (format or size) have been made to the
   * surface. After any structural changes, the preview is stopped and a new preview is started
   * after the {@link Camera} had a new {@link PreviewCallback} and a new preview display holder
   * set.
   * @param holder The {@code SurfaceHolder}  whose surface has changed
   * @param format The new {@link android.graphics.PixelFormat} of the surface (as {@code int})
   * @param width The new width of the surface
   * @param height The new height of the surface
   */
  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    if (this.holder.getSurface() != null) {
      try {
        camera.setPreviewCallback(new CameraCallback());
        camera.setPreviewDisplay(holder);
      } catch (IOException e) {
        dialog = LogUtils.logError(getContext(), getClass().getSimpleName(),
            getResources().getString(R.string.message_surface), e);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
          @Override
          public void onDismiss(DialogInterface dialog) {
            ((Activity) CameraPreview.this.context).finish();
          }
        });
      }

    }
  }

  /**
   * This is called immediately before a surface is being destroyed.
   * @param holder The SurfaceHolder whose surface is being destroyed
   */
  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {
    onPause();
  }

  /**
   * Set the {@link Camera}
   * @param camera The new {@code Camera}
   */
  public void setCamera(Camera camera) {
    this.camera = camera;
  }

  /**
   * This is called when the activity is paused. If the {@code Camera} is set, the
   * {@code PreviewCallback} will be set as {@code null} and the preview will be stopped.
   */
  public void onPause() {
    if (camera != null) {
      camera.setPreviewCallback(null);
      camera.stopPreview();
      camera.release();
      camera = null;
    }
  }
}
