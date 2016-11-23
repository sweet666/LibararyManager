package by.pvt.safronenko.library.tools.command.librarian.book;

import by.pvt.safronenko.library.tools.dao.BookDao;
import by.pvt.safronenko.library.beans.Author;
import by.pvt.safronenko.library.beans.Book;
import by.pvt.safronenko.library.tools.RequestUtils;
import by.pvt.safronenko.library.tools.StringUtils;
import by.pvt.safronenko.library.tools.command.ActionCommand;
import by.pvt.safronenko.library.tools.dao.AuthorDao;
import by.pvt.safronenko.library.tools.datasource.DataSourceHolder;
import by.pvt.safronenko.library.tools.resource.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

/**
 * Librarian: book add/update command.
 */
public class LibrarianBookAddCommand implements ActionCommand {

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
    public LibrarianBookAddCommand() {
        this.authorDao = new AuthorDao(DataSourceHolder.getDataSource());
        this.bookDao = new BookDao(DataSourceHolder.getDataSource());
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String isbn = StringUtils.trimToEmpty(request.getParameter("isbn"));
        String title = StringUtils.capitalizeFirst(request.getParameter("title"));
        Long authorId = RequestUtils.getLong(request, "authorId");

        if (isbn.isEmpty()) {
            request.setAttribute("error", "isbn");
        }
        if (title.isEmpty()) {
            request.setAttribute("error", "title");
        }
        if (authorId == null) {
            request.setAttribute("error", "author");
        }

        if (request.getAttribute("error") != null) {
            request.setAttribute("books", bookDao.getAll());
            request.setAttribute("authors", authorDao.getAll());
            return ConfigurationManager.getProperty("path.page.librarian.book");
        }

        Book book = null;
        if (!isbn.isEmpty()) {
            book = bookDao.get(isbn);
        }

        if (book == null) {
            book = new Book();
        }

        book.setTitle(title);
        book.setDescription(request.getParameter("description"));
        book.setEdition(Math.max(1, RequestUtils.getInteger(request, "edition", 1)));
        book.setYear(Math.max(1, RequestUtils.getInteger(request, "year", LocalDate.now().getYear())));
        book.setAuthor(new Author(authorId));

        if (book.getIsbn() != null) {
            bookDao.update(book);
        } else {
            book.setIsbn(isbn);
            bookDao.create(book);
        }

        // Redirect to editing or to all books.
        return redirectTo(response, "librarian_book_list");
    }
}
