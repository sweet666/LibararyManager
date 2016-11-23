package by.pvt.safronenko.library.tools.command.librarian.bookcopy;

import by.pvt.safronenko.library.tools.command.ActionCommand;
import by.pvt.safronenko.library.tools.dao.BookCopyDao;
import by.pvt.safronenko.library.tools.datasource.DataSourceHolder;
import by.pvt.safronenko.library.tools.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Librarian: book copy remove command.
 */
public class LibrarianBookCopyRemoveCommand implements ActionCommand {

    /**
     * Log4j logger (with logback implementation).
     */
    private static final Logger logger = Logger.getLogger(LibrarianBookCopyReleaseCommand.class);

    /**
     * Reference to book copy service.
     */
    private final BookCopyDao bookCopyDao;

    /**
     * Construct command.
     */
    public LibrarianBookCopyRemoveCommand() {
        this.bookCopyDao = new BookCopyDao(DataSourceHolder.getDataSource());
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String removeSerialNumber = StringUtils.trimToEmpty(request.getParameter("removeSerialNumber"));
        if (bookCopyDao.remove(removeSerialNumber)) {
            logger.warn(String.format("Book copy %s removed [%s].", removeSerialNumber, request.getRemoteAddr()));
        }

        // Redirect to editing or to all books' copies.
        return redirectTo(response, "librarian_book_copy_list");
    }
}
