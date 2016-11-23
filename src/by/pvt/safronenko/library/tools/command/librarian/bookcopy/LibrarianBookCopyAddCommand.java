package by.pvt.safronenko.library.tools.command.librarian.bookcopy;

import by.pvt.safronenko.library.beans.Book;
import by.pvt.safronenko.library.beans.BookCopy;
import by.pvt.safronenko.library.tools.command.ActionCommand;
import by.pvt.safronenko.library.tools.dao.BookCopyDao;
import by.pvt.safronenko.library.tools.dao.BookDao;
import by.pvt.safronenko.library.tools.datasource.DataSourceHolder;
import by.pvt.safronenko.library.tools.StringUtils;
import by.pvt.safronenko.library.tools.resource.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Librarian: book copy add command.
 */
public class LibrarianBookCopyAddCommand implements ActionCommand {

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
    public LibrarianBookCopyAddCommand() {
        this.bookDao = new BookDao(DataSourceHolder.getDataSource());
        this.bookCopyDao = new BookCopyDao(DataSourceHolder.getDataSource());
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String bookCopyPage = ConfigurationManager.getProperty("path.page.librarian.bookCopy");
        String serialNumber = StringUtils.trimToEmpty(request.getParameter("serialNumber"));
        if (serialNumber.isEmpty()) {
            request.setAttribute("books", bookDao.getAll());
            request.setAttribute("booksCopies", bookCopyDao.getAll());
            request.setAttribute("error", "serialNumber");
            return bookCopyPage;
        }

        String isbn = StringUtils.trimToEmpty(request.getParameter("isbn"));
        if (isbn.isEmpty()) {
            request.setAttribute("books", bookDao.getAll());
            request.setAttribute("booksCopies", bookCopyDao.getAll());
            request.setAttribute("error", "isbn");
            return bookCopyPage;
        }

        BookCopy bookCopy = null;
        if (!serialNumber.isEmpty()) {
            bookCopy = bookCopyDao.get(serialNumber);
        }

        if (bookCopy == null) {
            bookCopy = new BookCopy();
        }

        bookCopy.setBook(new Book(isbn));

        if (bookCopy.getSerialNumber() != null) {
            bookCopyDao.update(bookCopy);
        } else {
            bookCopy.setSerialNumber(serialNumber);
            bookCopyDao.create(bookCopy);
        }

        // Redirect to editing or to all books' copies.
        return redirectTo(response, "librarian_book_copy_list");
    }
}
