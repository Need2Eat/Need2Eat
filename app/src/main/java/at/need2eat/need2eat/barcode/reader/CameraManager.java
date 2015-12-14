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
  public CameraManager() throws CameraNotFoundException {
    camera = CameraManager.createCameraInstance();
  }

  /**
   * Identifies the back-facing (or any other) camera of the device and opens a new connection to it
   * @return The new {@link Camera} object
   * @throws RuntimeException if opening the camera fails (for example, if the camera is in use
   * by another process or device policy manager has disabled the camera).
   * @throws CameraNotFoundException if no camera is available
   */
  private static Camera createCameraInstance() throws CameraNotFoundException {

    CameraInfo cameraInfo = new CameraInfo();
    int number = Camera.getNumberOfCameras();

    if(number == 0) {
      throw new CameraNotFoundException();
    }

    int cameraId = 0;

    for (int i = 0; i < number; i++) {
      Camera.getCameraInfo(i, cameraInfo);
      cameraId = i;
      if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
        break;
      }
    }

    return Camera.open(cameraId);
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
   * @throws CameraNotFoundException if no camera is available
   */
  public void onResume() throws CameraNotFoundException {
    if (camera == null) {
      camera = createCameraInstance();
    }
  }

}
