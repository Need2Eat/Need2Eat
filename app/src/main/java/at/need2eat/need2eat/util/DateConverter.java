package at.need2eat.need2eat.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * This is a utility class providing methods to convert a {@code String} into a {@link Date} and
 * vice versa
 * @author Maxi Nothnagel - mx.nothnagel@gmail.com
 */
public class DateConverter {

  public static Date getDateFromString(String expiryDate) throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("dd.mm.yyyy", Locale.GERMANY);
    return sdf.parse(expiryDate);
  }

  public static String getStringFromDate(Date expiryDate) throws IllegalArgumentException {
    if (expiryDate == null) {
      throw new IllegalArgumentException();
    }
    SimpleDateFormat sdf = new SimpleDateFormat("dd.mm.yyyy", Locale.GERMANY);
    return sdf.format(expiryDate);
  }
}
