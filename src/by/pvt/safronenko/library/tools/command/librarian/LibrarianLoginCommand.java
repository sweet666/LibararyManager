package by.pvt.safronenko.library.tools.command.librarian;

import by.pvt.safronenko.library.beans.User;
import by.pvt.safronenko.library.tools.RequestUtils;
import by.pvt.safronenko.library.tools.command.ActionCommand;
import by.pvt.safronenko.library.tools.dao.UserDao;
import by.pvt.safronenko.library.tools.datasource.DataSourceHolder;
import by.pvt.safronenko.library.enums.UserRole;
import by.pvt.safronenko.library.tools.resource.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * LibrarianLoginCommand.
 */
public class LibrarianLoginCommand implements ActionCommand {

    /**
     * Reference to user DAO.
     */
    private final UserDao userDao;

    /**
     * Constructs command.
     */
    public LibrarianLoginCommand() {
        this.userDao = new UserDao(DataSourceHolder.getDataSource());
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String usernameOrEmail = RequestUtils.get(request, "username", "");
        String password = RequestUtils.get(request, "password", "");

        if (!usernameOrEmail.isEmpty()) {
            User user = userDao.signin(usernameOrEmail, password);
            if (user != null && UserRole.LIBRARIAN.equals(user.getRole())) {

                // Remember role and username in session.
                request.getSession().setAttribute("loggedInRole", user.getRole().toString().toLowerCase());
                request.getSession().setAttribute("loggedInUser", user.getUsername());

                // Redirect.
                return redirectTo(response, "librarian_index");
            }
        }

        request.setAttribute("librarianError", "bad_username");
        return ConfigurationManager.getProperty("path.page.login");
    }
}
