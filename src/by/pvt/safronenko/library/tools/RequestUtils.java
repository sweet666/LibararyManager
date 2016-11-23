package by.pvt.safronenko.library.tools;

import javax.servlet.http.HttpServletRequest;

/**
 * Request utilities.
 */
public final class RequestUtils {

    /**
     * Returns parameter value or default value if there is no  parameter in the request.
     *
     * @param request      Request.
     * @param name         Parameter name.
     * @param defaultValue Default value.
     * @return Parameter value or default value.
     */
    public static String get(HttpServletRequest request, String name, String defaultValue) {
        String value = request.getParameter(name);
        if (value != null) {
            return value;
        }
        return defaultValue;
    }

    /**
     * Returns parameter value or default value if there is no  parameter in the request.
     *
     * @param request      Request.
     * @param name         Parameter name.
     * @param defaultValue Default value.
     * @return Parameter value or default value.
     */
    public static long getLong(HttpServletRequest request, String name, int defaultValue) {
        Long value = getLong(request, name);
        if (value != null) {
            return value;
        }
        return defaultValue;
    }

    /**
     * Returns long or null, if not present.
     *
     * @param request      Request.
     * @param name         Parameter name.
     * @return Parameter value or null.
     */
    public static Long getLong(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    /**
     * Returns parameter value or default value if there is no  parameter in the request.
     *
     * @param request      Request.
     * @param name         Parameter name.
     * @param defaultValue Default value.
     * @return Parameter value or default value.
     */
    public static int getInteger(HttpServletRequest request, String name, int defaultValue) {
        Integer value = getInteger(request, name);
        if (value != null) {
            return value;
        }
        return defaultValue;
    }

    /**
     * Returns integer or null, if not present.
     *
     * @param request      Request.
     * @param name         Parameter name.
     * @return Parameter value or null.
     */
    public static Integer getInteger(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    private RequestUtils() {
    }
}
