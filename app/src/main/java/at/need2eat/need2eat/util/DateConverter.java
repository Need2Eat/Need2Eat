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

  /**
   * Get a {@link Date} from a {@code String}
   * @param expiryDate The {@code String} that should be converted to a {@code Date}
   * @return The converted {@code Date}
   * @throws ParseException if the {@code String} could not be converted
   */
  public static Date getDateFromString(String expiryDate) throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("dd.mm.yyyy", Locale.GERMANY);
    return sdf.parse(expiryDate);
  }

  /**
   * Get a {@code String} from a {@link Date}
   * @param expiryDate The {@code Date} that should be converted to a {@code String}
   * @return The converted {@code String}
   * @throws IllegalArgumentException if the given {@code Date} is null
   */
  public static String getStringFromDate(Date expiryDate) throws IllegalArgumentException {
    if (expiryDate == null) {
      throw new IllegalArgumentException();
    }
    SimpleDateFormat sdf = new SimpleDateFormat("dd.mm.yyyy", Locale.GERMANY);
    return sdf.format(expiryDate);
  }
}
