package by.pvt.safronenko.library.tools.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Action command interface.
 */
public interface ActionCommand {

    /**
     * Name of the fake redirect page to return from action in case of redirect.
     */
    String REDIRECT_PAGE = "/?#?/#/#/#/_redirect_page";

    /**
     * Executes action.
     *
     * @param request  Http request.
     * @param response Http response.
     * @return JSP page name.
     */
    String execute(HttpServletRequest request, HttpServletResponse response);

    /**
     * Redirect method that sends Location header with given c
     *
     * @param response Http response.
     * @param command  Command to redirect to.
     * @return Fake redirect page that can be returned to front controller.
     */
    default String redirectTo(HttpServletResponse response, String command) {
        response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
        response.setHeader("Location", "./Controller?command=" + command);
        return REDIRECT_PAGE;
    }

    /**
     * Encodes query like URI component.
     *
     * @param query Query.
     * @return Url encoded query.
     */
    default String urlEncode(String query) {
        try {
            return URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Error while encoding: " + query, e);
        }
    }
}