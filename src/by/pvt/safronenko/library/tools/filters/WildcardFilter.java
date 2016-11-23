package by.pvt.safronenko.library.tools.filters;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Wildcard filter for all URLs: jsp and controllers.
 */
public class WildcardFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        // Set common variables for all JSP.
        ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of("Europe/Minsk"));
        String dateAsString = String.format("%d-%02d-%02d %02d:%02d",
                dateTime.getYear(),
                dateTime.getMonth().getValue(),
                dateTime.getDayOfMonth(),
                dateTime.getHour(),
                dateTime.getMinute());
        servletRequest.setAttribute("date", dateAsString);

        // Look into the session to get user role info.
        HttpSession session =  ((HttpServletRequest) servletRequest).getSession();
        servletRequest.setAttribute("loggedInRole",
                session.getAttribute("loggedInRole") != null ?
                        session.getAttribute("loggedInRole") :
                        "none");
        servletRequest.setAttribute("loggedInUser", session.getAttribute("loggedInUser"));

        // Proceed.
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
