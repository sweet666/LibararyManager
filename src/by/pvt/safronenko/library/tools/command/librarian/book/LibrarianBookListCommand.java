package by.pvt.safronenko.library.tools.command.librarian.book;

import by.pvt.safronenko.library.tools.dao.BookDao;
import by.pvt.safronenko.library.beans.Book;
import by.pvt.safronenko.library.tools.StringUtils;
import by.pvt.safronenko.library.tools.command.ActionCommand;
import by.pvt.safronenko.library.tools.dao.AuthorDao;
import by.pvt.safronenko.library.tools.datasource.DataSourceHolder;
import by.pvt.safronenko.library.tools.resource.ConfigurationManager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Librarian: book list command.
 */
public class LibrarianBookListCommand implements ActionCommand {

    /**
     * Log4j logger (with logback implementation).
     */
    private static final Logger logger = Logger.getLogger(LibrarianBookListCommand.class);

    /**
     * Reference to book service.
     */
    private final BookDao bookDao;

    /**
     * Author service.
     */
    private final AuthorDao authorDao;

    /**
     * Constructs book controller.
     */
    public LibrarianBookListCommand() {
        this.authorDao = new AuthorDao(DataSourceHolder.getDataSource());
        this.bookDao = new BookDao(DataSourceHolder.getDataSource());
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("books", bookDao.getAll());
        request.setAttribute("authors", authorDao.getAll());

        String isbn = StringUtils.trimToEmpty(request.getParameter("isbn"));
        if (!isbn.isEmpty()) {
            Book existing = bookDao.get(isbn);
            if (existing != null) {
                request.setAttribute("isbn", existing.getIsbn());
                request.setAttribute("title", existing.getTitle());
                request.setAttribute("description", existing.getDescription());
                request.setAttribute("edition", existing.getEdition());
                request.setAttribute("year", existing.getYear());
                request.setAttribute("authorId", existing.getAuthor().getId());
            } else {
                logger.warn("Unable to edit non-existing book: " + isbn);
            }
        }

        return ConfigurationManager.getProperty("path.page.librarian.book");
    }
}
