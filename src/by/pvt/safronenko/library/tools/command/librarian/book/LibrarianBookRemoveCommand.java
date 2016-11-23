package by.pvt.safronenko.library.tools.command.librarian.book;

import by.pvt.safronenko.library.tools.dao.BookDao;
import by.pvt.safronenko.library.tools.StringUtils;
import by.pvt.safronenko.library.tools.command.ActionCommand;
import by.pvt.safronenko.library.tools.datasource.DataSourceHolder;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * LibrarianBookRemoveCommand.
 */
public class LibrarianBookRemoveCommand implements ActionCommand {

    /**
     * Reference to book service.
     */
    private final BookDao bookDao;

    /**
     * Log4j logger (with logback implementation).
     */
    private static final Logger logger = Logger.getLogger(LibrarianBookRemoveCommand.class);

    /**
     * Constructs book controller.
     */
    public LibrarianBookRemoveCommand() {
        this.bookDao = new BookDao(DataSourceHolder.getDataSource());
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String removeIsbn = StringUtils.trimToEmpty(request.getParameter("removeId"));
        if (bookDao.remove(removeIsbn)) {
            logger.warn(String.format("Book %s removed [%s].", removeIsbn, request.getRemoteAddr()));
        }
        return redirectTo(response, "librarian_book_list");
    }
}
