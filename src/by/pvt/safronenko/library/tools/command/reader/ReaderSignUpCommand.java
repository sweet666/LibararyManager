package by.pvt.safronenko.library.tools.command.reader;

import by.pvt.safronenko.library.beans.User;
import by.pvt.safronenko.library.enums.UserRole;
import by.pvt.safronenko.library.exceptions.UserCreationException;
import by.pvt.safronenko.library.tools.RequestUtils;
import by.pvt.safronenko.library.tools.command.ActionCommand;
import by.pvt.safronenko.library.tools.dao.UserDao;
import by.pvt.safronenko.library.tools.datasource.DataSourceHolder;
import by.pvt.safronenko.library.tools.StringUtils;
import by.pvt.safronenko.library.tools.resource.ConfigurationManager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sign up.
 */
public class ReaderSignUpCommand implements ActionCommand {

    /**
     * Logger.
     */
    private static final Logger logger = Logger.getLogger(ReaderSignUpCommand.class);

    /**
     * Reference to user DAO.
     */
    private final UserDao userDao;

    /**
     * Constructs command.
     */
    public ReaderSignUpCommand() {
        this.userDao = new UserDao(DataSourceHolder.getDataSource());
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String username = StringUtils.trimToEmpty(RequestUtils.get(request, "username", ""));
        String email = StringUtils.trimToEmpty(RequestUtils.get(request, "email", ""));
        String password = RequestUtils.get(request, "password", "");
        String passwordRepeat = RequestUtils.get(request, "password_repeat", "");

        if (username.isEmpty() || username.length() > 100) {
            request.setAttribute("error", "bad_username");
        } else if (email.isEmpty() || !email.contains("@") || !email.contains(".")) {
            request.setAttribute("error", "bad_email");
        } else if (password.isEmpty() || !password.equals(passwordRepeat)) {
            request.setAttribute("error", "bad_password");
        }
        else {
            try {
                User user = userDao.createUser(username, email, password, UserRole.READER);
                request.getSession().setAttribute("loggedInRole", user.getRole().toString().toLowerCase());
                request.getSession().setAttribute("loggedInUser", user.getUsername());

                // Redirect to reader.
                return redirectTo(response, "reader_book_search");
            } catch (UserCreationException e) {
                logger.error("Error while signup: " + username + "/" + email, e);
                request.setAttribute("signup_error", e.getMessage());
            }
        }
        request.setAttribute("username", username);
        request.setAttribute("email", email);
        return ConfigurationManager.getProperty("path.page.reader.signup");
    }
}
