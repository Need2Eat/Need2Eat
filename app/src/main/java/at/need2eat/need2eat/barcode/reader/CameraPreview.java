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
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.client.result.ExpandedProductParsedResult;
import com.google.zxing.client.result.ExpandedProductResultParser;
import com.google.zxing.common.HybridBinarizer;

import java.io.IOException;

import at.need2eat.need2eat.EditActivity;
import at.need2eat.need2eat.LogUtils;
import at.need2eat.need2eat.R;

/**
 * This class creates a new {@link SurfaceView} which is used to draw the preview frames of the
 * image captures by the {@link Camera}. These preview frames are converted to a
 * {@link BinaryBitmap} and decoded by a {@link MultiFormatReader}. The decoded {@link Result} is
 * parsed into an {@link ExpandedProductParsedResult} which contains further information about the
 * product. This {@code ExpandedProductParsedResult} is sent to the {@link EditActivity} to insert
 * a new product.
 * @author Maxi Nothnagel - mx.nothnagel@gmail.com
 */
class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

  private SurfaceHolder holder;
  private Camera camera;
  private int width;
  private int height;
  private MultiFormatReader reader;
  private int left;
  private int top;
  private int areaWidth;
  private int areaHeight;
  private AlertDialog dialog;
  private Context context;

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

    width = 640;
    height = 480;

    params.setPreviewSize(width, height);
    camera.setParameters(params);

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
          "Surface zur Vorschau-Anzeige nicht verfügbar!", e);

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
        camera.stopPreview();
        camera.setPreviewCallback(callback);
        camera.setPreviewDisplay(holder);
        camera.startPreview();
      } catch (IOException e) {
        dialog = LogUtils.logError(getContext(), getClass().getSimpleName(),
            "Surface zur Vorschau-Anzeige nicht verfügbar!", e);

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
    }
  }

  /**
   * Sets the area size for the preview frames of the camera
   * @param left The left offset
   * @param top The top offset
   * @param width The width of the display
   */
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

      LuminanceSource source =
          new PlanarYUVLuminanceSource(data, width, height, left, top,
              areaWidth, areaHeight, false);
      BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
      Result result;

      try {
        result = reader.decode(bitmap, null);

        if (result != null) {
          ExpandedProductResultParser parser = new ExpandedProductResultParser();
          ExpandedProductParsedResult expandedResult = parser.parse(result);

          Resources resources = getResources();

          Intent intent = new Intent(context, EditActivity.class);
          intent.putExtra(resources.getString(R.string.extra_gtin), expandedResult.getProductID());
          intent.putExtra(resources.getString(R.string.extra_expiry),
              expandedResult.getExpirationDate());
          intent.putExtra(resources.getString(R.string.extra_origin), ScannerActivity.class);
          context.startActivity(intent);
        }

      } catch (NotFoundException e) {
        dialog = LogUtils.logError(getContext(), getClass().getSimpleName(),
            "Stellen Sie sicher, dass der Barcode nicht abgedeckt ist!", e);
      }
    }
  };
}
