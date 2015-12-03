package at.need2eat.need2eat.barcode.reader;


import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;

public class CameraManager {

  private Camera camera;

  public CameraManager() {
    this.camera = CameraManager.createCameraInstance();
  }

  private static Camera createCameraInstance() throws RuntimeException {

    int backId = 0;
    CameraInfo cameraInfo = new CameraInfo();
    int number = Camera.getNumberOfCameras();

    for (int i = 0; i < number; i++) {
      Camera.getCameraInfo(i, cameraInfo);
      if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
        backId = i;
      }
    }

    return Camera.open(backId);
  }

  public Camera getCamera() {
    return camera;
  }

  private void releaseCamera() {
    if (camera != null) {
      camera.release();
      camera = null;
    }
  }

  public void onPause() {
    releaseCamera();
  }

  public void onResume() {
    if (camera == null) {
      camera = createCameraInstance();
    }
  }

}
