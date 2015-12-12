package at.need2eat.need2eat.barcode.reader;


import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;

/**
 * The {@code CameraManager} class is used to create a new {@link Camera} as the back-facing
 * camera of the device
 * @author Maxi Nothnagel - mx.nothnagel@gmail.com
 */
class CameraManager {

  private Camera camera;

  /**
   * Default constructor. The {@link Camera} is created by {@link #createCameraInstance()}
   */
  public CameraManager() {
    camera = CameraManager.createCameraInstance();
  }

  /**
   * Identifies the back-facing camera of the device and opens a new connection to it
   * @return The new {@link Camera} object
   * @throws RuntimeException if opening the camera fails (for example, if the camera is in use
   * by another process or device policy manager has disabled the camera).
   */
  private static Camera createCameraInstance() {

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

  /**
   * Returns the {@link Camera} object of the {@code CameraManager}
   * @return The {@code Camera} object
   */
  public Camera getCamera() {
    return camera;
  }

  /**
   * Releases the camera in order to make it usable by other applications or activities
   */
  private void releaseCamera() {
    if (camera != null) {
      camera.release();
      camera = null;
    }
  }

  /**
   * Calls {@link #releaseCamera()} in order to release the camera when the activity is paused
   */
  public void onPause() {
    releaseCamera();
  }

  /**
   * Creates a new connection to the back-facing camera when the activity is resumed
   */
  public void onResume() {
    if (camera == null) {
      camera = createCameraInstance();
    }
  }

}
