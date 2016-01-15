package at.need2eat.need2eat.barcode.reader;

import java.util.HashMap;
import java.util.Map;

/**
 * This enum is used to identify the different kinds of AIs (Application Identifiers) of a GS1-128
 * barcode and the lengths of their values. Every AI with a code lower than 400x are supported,
 * every other AI will be listed as {@link BarcodeAi#UNCOMMON_AI}
 * @author Maxi Nothnagel - mx.nothnagel@gmail.com
 */
public enum BarcodeAi {
  AI_2, AI_6, AI_14, AI_18, VARIABLE, UNCOMMON_AI;

  private int length;

  BarcodeAi() {
    try {
      this.length = Integer.parseInt(this.name().substring(3));
    } catch (NumberFormatException e) {
      this.length = -1;
    }
  }

  /**
   * Get the length of the value of the AI. If the AI is {@link BarcodeAi#VARIABLE} or an
   * {@link BarcodeAi#UNCOMMON_AI}, the length is -1
   * @return the length
   */
  public int getLength() {
    return this.length;
  }

  /**
   * Get the type of a given AI as a {@link BarcodeAi}
   * @param ai the AI
   * @return the {@link BarcodeAi}. If the AI is an uncommon AI (i.e. every AI with a number greater
   * than 400x), {@link BarcodeAi#UNCOMMON_AI} is returned
   */
  public static BarcodeAi findAiType(String ai) {
    switch (ai.substring(0, 2)) {
      case "00":
        return AI_18;
      case "01":
      case "02":
        return AI_14;
      case "10":
      case "30":
      case "37":
      case "39":
        return VARIABLE;
      case "11":
      case "12":
      case "13":
      case "15":
      case "17":
        return AI_6;
      case "20":
        return AI_2;
      default:
        switch (ai.substring(0, 1)) {
          case "2":
            return VARIABLE;
          case "3":
            return AI_6;
          default:
            return UNCOMMON_AI;
        }
    }
  }

  /**
   * Find the first AI in the given text
   * @param text the text which should be analysed
   * @return the first AI code as a {@code String} found in the text or "" if no AI could be found
   */
  private static String findFirstAi(String text) {
    switch (text.charAt(0)) {
      case '0':
      case '1':
        return text.substring(0, 2);
      case '2':
        return text.substring(0, (Integer.parseInt(text.substring(1, 2)) < 3) ? 2 : 3);
      case '3':
        return text.substring(0,
            (text.substring(1, 2).equals("0") || text.substring(1, 2).equals("7")) ? 2 : 4);
      default:
        return "";
    }
  }


  /**
   * Find every AI and its value in a given text
   * @param text the text that should be analysed
   * @return a {@link Map} with the AI codes as keys and the AIs´ values as values
   */
  public static Map<String, String> findAiValues(String text) {
    Map<String, String> result = new HashMap<>();

    // Filter the GS1 indicator character
    if (text.contains("]C1")) {
      text = text.substring(3);
    }

    /*
    We split the barcode text on every occasion of the unicode character \u001D. This character is
    used to identify the end of AIs with variable length. As it only appears at the end of every
    variable AI, there can only be one AI with variable length in an array item
     */
    String[] separatedValues = text.split("\u001D");

    for (String value: separatedValues) {

      while (true) {
        String ai = BarcodeAi.findFirstAi(value);
        int length = BarcodeAi.findAiType(ai).getLength();

        /*
        if the length is -1, the BarcodeAi length is VARIABLE, thus it is the last AI in this string
         */
        if (length == -1) {
          // and everything besides the indicator
          result.put(ai, value.substring(ai.length()));
          // since there can't be any other AIs after a variable one, we can break the while loop
          break;
        } else {
          int totalLength = ai.length() + length;
          result.put(ai, value.substring(ai.length(), totalLength));

          /*
          if the indicator and its content fill the whole string, we can break the loop and go to
          the next separated string as there can't be any other AIs. Otherwise, we just remove the
          already known AI and its content.
           */
          if (value.length() == totalLength) {
            break;
          } else {
            value = value.substring(totalLength);
          }
        }
      }
    }

    return result;
  }
}
