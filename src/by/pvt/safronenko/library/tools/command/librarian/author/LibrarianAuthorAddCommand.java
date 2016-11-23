package by.pvt.safronenko.library.tools.command.librarian.author;

import by.pvt.safronenko.library.beans.Author;
import by.pvt.safronenko.library.tools.RequestUtils;
import by.pvt.safronenko.library.tools.command.ActionCommand;
import by.pvt.safronenko.library.tools.dao.AuthorDao;
import by.pvt.safronenko.library.tools.datasource.DataSourceHolder;
import by.pvt.safronenko.library.tools.StringUtils;
import by.pvt.safronenko.library.tools.resource.ConfigurationManager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Librarian: author add command.
 */
public class LibrarianAuthorAddCommand implements ActionCommand {

    /**
     * Log4j logger (with logback implementation).
     */
    private static final Logger logger = Logger.getLogger(LibrarianAuthorAddCommand.class);

    /**
     * Reference to author service.
     */
    private final AuthorDao authorDao;

    /**
     * Constructs command.
     */
    public LibrarianAuthorAddCommand() {
        this.authorDao = new AuthorDao(DataSourceHolder.getDataSource());
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String firstName = StringUtils.capitalize(request.getParameter("firstName"));
        String lastName = StringUtils.capitalize(request.getParameter("lastName"));
        String middleName = request.getParameter("middleName");

        if (middleName != null) {
            middleName = StringUtils.capitalize(middleName);
        }

        if (lastName.isEmpty() || firstName.isEmpty()) {
            request.setAttribute("error", "name");
            return ConfigurationManager.getProperty("path.page.librarian.author");
        }

        Long id = RequestUtils.getLong(request, "id");
        if (id != null && id > 0) {
            Author existing = authorDao.get(id);
            if (existing != null) {
                existing.setFirstName(firstName);
                existing.setLastName(lastName);
                existing.setMiddleName(middleName);
                authorDao.update(existing);
            } else {
                logger.warn("Unable to update non-existing author: " + id);
            }
        } else {
            authorDao.create(firstName, middleName, lastName);
        }

        // Redirect to editing or to all authors.
        if (id != null) {
            return redirectTo(response, "librarian_author_list&id=" + id);
        }
        return redirectTo(response, "librarian_author_list");
    }
}
