package by.pvt.safronenko.library.tools.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * LogoutCommand.
 */
public class LogoutCommand implements ActionCommand {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        // Forget the user and the role.
        request.getSession().removeAttribute("loggedInRole");
        request.getSession().removeAttribute("loggedInUser");

        // Redirect back to login.
        response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
        response.setHeader("Location", "./Controller?command=index");
        return REDIRECT_PAGE;
    }
}
