package by.pvt.safronenko.library.tools.filters;

import by.pvt.safronenko.library.tools.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Authentication filter: checks access to pages / servlets for different roles.
 */
public class AuthenticationFilter implements Filter {

    private static final Logger logger = Logger.getLogger(AuthenticationFilter.class);

    /**
     * Reader accessible URLs.
     */
    private static final String[] READER_URLS = new String[]{
            "reader_book_search",
            "reader_book_release"
    };

    /**
     * Librarian accessible URLs.
     */
    private static final String[] LIBRARIAN_URLS = new String[]{
            "librarian_book_copy",
            "librarian_book",
            "librarian_author",
            "librarian_index"
    };

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String servletPage = StringUtils.trimToEmpty(request.getServletPath()) + "?command=" + request.getParameter("command");
        HttpSession session = request.getSession();

        // User role or "none" if the user is not logged in.
        String userRole =
                session.getAttribute("loggedInRole") != null ? (String) session.getAttribute("loggedInRole") : "none";

        boolean forbidden;

        switch (userRole) {
            case "none":
                forbidden = matchesLibrarianUrl(servletPage) || matchesReaderUrl(servletPage);
                break;
            case "reader":
                forbidden = matchesLibrarianUrl(servletPage);
                break;
            case "librarian":
                // All URLs are allowed for librarian.
            default:
                forbidden = false;
        }

        // If the page is forbidden log attempt and redirect to index page.
        if (forbidden) {
            String message = String.format("Url is forbidden for role %s/[%s]: %s?%s. Redirecting to index.",
                    userRole,
                    request.getRemoteAddr(),
                    request.getServletPath(),
                    StringUtils.trimToEmpty(request.getQueryString()));
            logger.warn(message);

            // Redirect back to index (with login).
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
            response.setHeader("Location", "./Controller?command=index");
            return;
        }

        // Continue work.
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * Checks whether given servlet path matches one of th reader URLs.
     *
     * @param servletPath Servlet path.
     * @return whether given servlet path matches one of th reader URLs.
     */
    private static boolean matchesReaderUrl(String servletPath) {
        for (String readerUrl : READER_URLS) {
            if (servletPath.contains(readerUrl)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether given servlet path matches one of th reader URLs.
     *
     * @param servletPath Servlet path.
     * @return whether given servlet path matches one of th reader URLs.
     */
    private static boolean matchesLibrarianUrl(String servletPath) {
        for (String readerUrl : LIBRARIAN_URLS) {
            if (servletPath.contains(readerUrl)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {
    }
}
