package by.pvt.safronenko.library.tools.command.librarian.author;

import by.pvt.safronenko.library.tools.RequestUtils;
import by.pvt.safronenko.library.tools.command.ActionCommand;
import by.pvt.safronenko.library.tools.dao.AuthorDao;
import by.pvt.safronenko.library.tools.datasource.DataSourceHolder;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Librarian: author remove command.
 */
public class LibrarianAuthorRemoveCommand implements ActionCommand {

    /**
     * Log4j logger (with logback implementation).
     */
    private static final Logger logger = Logger.getLogger(LibrarianAuthorRemoveCommand.class);

    /**
     * Reference to author service.
     */
    private final AuthorDao authorDao;

    public LibrarianAuthorRemoveCommand() {
        this.authorDao = new AuthorDao(DataSourceHolder.getDataSource());
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Long id = RequestUtils.getLong(request, "removeId", -1);
        if (authorDao.remove(id)) {
            logger.warn("Author " + id + " removed. " + request.getRemoteAddr());
        }
        return redirectTo(response, "librarian_author_list");
    }
}
