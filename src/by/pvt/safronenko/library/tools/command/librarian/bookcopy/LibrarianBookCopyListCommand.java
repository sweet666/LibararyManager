package by.pvt.safronenko.library.tools.command.librarian.bookcopy;

import by.pvt.safronenko.library.tools.dao.BookCopyDao;
import by.pvt.safronenko.library.tools.dao.BookDao;
import by.pvt.safronenko.library.beans.BookCopy;
import by.pvt.safronenko.library.tools.StringUtils;
import by.pvt.safronenko.library.tools.command.ActionCommand;
import by.pvt.safronenko.library.tools.datasource.DataSourceHolder;
import by.pvt.safronenko.library.tools.resource.ConfigurationManager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Librarian: book copy list command.
 */
public class LibrarianBookCopyListCommand implements ActionCommand {

    /**
     * Log4j logger (with logback implementation).
     */
    private static final Logger logger = Logger.getLogger(LibrarianBookCopyListCommand.class);

    /**
     * Reference to book service.
     */
    private final BookDao bookDao;

    /**
     * Reference to book copy service.
     */
    private final BookCopyDao bookCopyDao;

    /**
     * Constructs book copy command.
     */
    public LibrarianBookCopyListCommand() {
        this.bookDao = new BookDao(DataSourceHolder.getDataSource());
        this.bookCopyDao = new BookCopyDao(DataSourceHolder.getDataSource());
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("books", bookDao.getAll());
        request.setAttribute("booksCopies", bookCopyDao.getAll());

        String serialNumber = StringUtils.trimToEmpty(request.getParameter("serialNumber"));
        if (!serialNumber.isEmpty()) {
            BookCopy existing = bookCopyDao.get(serialNumber);
            if (existing != null) {
                request.setAttribute("serialNumber", existing.getSerialNumber());
                request.setAttribute("isbn", existing.getBook().getIsbn());
                request.setAttribute("edit", true);
            } else {
                logger.warn("Unable to edit non-existing book copy: " + serialNumber);
            }
        }
        if (request.getAttribute("edit") == null) {
            request.setAttribute("edit", false);
            request.setAttribute("serialNumber", UUID.randomUUID().toString());
        }
        return ConfigurationManager.getProperty("path.page.librarian.bookCopy");
    }
}
