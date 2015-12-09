package at.need2eat.need2eat.barcode.reader;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.widget.FrameLayout;

import at.need2eat.need2eat.R;

public class ScannerActivity extends AppCompatActivity {

  private CameraPreview camPreview;
  private CameraManager manager;
  private HoverView view;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_scanner);
    Display display = getWindowManager().getDefaultDisplay();
    view = (HoverView) findViewById(R.id.hover_view);
    Point size = new Point();
    display.getSize(size);
    view.update(size.x, size.y);

    manager = new CameraManager();
    camPreview = new CameraPreview(this, manager.getCamera());
    camPreview.setArea(view.getHoverLeft(), view.getHoverTop(), size.x);

    FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
    preview.addView(camPreview);
  }

  @Override
  protected void onPause() {
    super.onPause();
    camPreview.onPause();
    manager.onPause();
  }

  @Override
  protected void onResume() {
    super.onResume();
    manager.onResume();
    camPreview.setCamera(manager.getCamera());
  }
}
