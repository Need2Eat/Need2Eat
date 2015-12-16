package at.need2eat.need2eat.barcode.reader;

/**
 * This exception gets thrown whenever the application is not able to connect to a camera device.
 * @author Maxi Nothnagel - mx.nothnage.@gmail.com
 */
public class CameraNotAccessibleException extends Exception {

  @Override
  public String getMessage() {
    return "Cannot connect to camera. Assure yourself that the camera is available.";
  }
}
