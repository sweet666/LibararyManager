package by.pvt.safronenko.library.tools.command.librarian.author;

import by.pvt.safronenko.library.tools.dao.AuthorDao;
import by.pvt.safronenko.library.tools.datasource.DataSourceHolder;
import by.pvt.safronenko.library.beans.Author;
import by.pvt.safronenko.library.tools.StringUtils;
import by.pvt.safronenko.library.tools.command.ActionCommand;
import by.pvt.safronenko.library.tools.resource.ConfigurationManager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Librarian: author list command.
 */
public class LibrarianAuthorListCommand implements ActionCommand {

    /**
     * Log4j logger (with logback implementation).
     */
    private static final Logger logger = Logger.getLogger(LibrarianAuthorListCommand.class);

    /**
     * Reference to author service.
     */
    private final AuthorDao authorDao;

    /**
     * Constructs command.
     */
    public LibrarianAuthorListCommand() {
        this.authorDao = new AuthorDao(DataSourceHolder.getDataSource());
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("authors", authorDao.getAll());

        String id = StringUtils.trimToEmpty(request.getParameter("id"));
        if (!id.isEmpty()) {
            Author existing = authorDao.get(Long.parseLong(id));
            if (existing != null) {
                request.setAttribute("firstName", existing.getFirstName());
                request.setAttribute("lastName", existing.getLastName());
                request.setAttribute("middleName", existing.getMiddleName());
                request.setAttribute("id", existing.getId());
            } else {
                logger.warn("Unable to edit non-existing author: " + id);
            }
        }

        return ConfigurationManager.getProperty("path.page.librarian.author");
    }
}
