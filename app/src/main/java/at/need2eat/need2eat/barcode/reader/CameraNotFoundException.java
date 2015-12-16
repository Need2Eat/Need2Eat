package at.need2eat.need2eat.barcode.reader;

/**
 * This exception gets thrown whenever the application is not able to find any camera devices.
 * @author Maxi Nothnagel - mx.nothnage.@gmail.com
 */
public class CameraNotFoundException extends Exception {

  @Override
  public String getMessage() {
    return "No camera available";
  }

}
