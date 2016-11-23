package by.pvt.safronenko.library.tools;

/**
 * String utils.
 */
public final class StringUtils {

    /**
     * Replaces null with empty string.
     *
     * @param string String.
     * @return Trimmed string or empty if null.
     */
    public static String trimToEmpty(String string) {
        if (string == null) {
            return "";
        }
        return string.trim();
    }

    /**
     * Checks if the trimmed string is empty and returns null instead.
     *
     * @param string String.
     * @return Trimmed string or null if empty.
     */
    public static String trimToNull(String string) {
        if (string == null) {
            return null;
        }
        string = string.trim();
        if (string.isEmpty()) {
            return null;
        }
        return string;
    }

    /**
     * Capitalizes all the words in the given string. If null is given returns empty string.
     *
     * @param string Word.
     * @return Capitalized string.
     */
    public static String capitalize(String string) {
        if (string == null) {
            return "";
        }
        StringBuilder result = new StringBuilder(string.trim().toLowerCase());
        for (int i = 0; i < result.length(); i++) {
            if (i == 0 || Character.isWhitespace(result.charAt(i - 1))) {
                result.setCharAt(i, Character.toUpperCase(result.charAt(i)));
            }
        }
        return result.toString();
    }

    /**
     * Capitalizes the first word in the given string. If null is given returns empty string.
     *
     * @param string Word.
     * @return Capitalized string.
     */
    public static String capitalizeFirst(String string) {
        if (string == null) {
            return "";
        }
        StringBuilder result = new StringBuilder(string.trim().toLowerCase());
        result.setCharAt(0, Character.toUpperCase(result.charAt(0)));
        return result.toString();
    }

    private StringUtils() {
    }
}
