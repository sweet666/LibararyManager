package by.pvt.safronenko.library.tools.command.reader;

import by.pvt.safronenko.library.tools.command.ActionCommand;
import by.pvt.safronenko.library.tools.dao.BookCopyDao;
import by.pvt.safronenko.library.tools.datasource.DataSourceHolder;
import by.pvt.safronenko.library.tools.StringUtils;
import by.pvt.safronenko.library.tools.resource.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ReaderBookSearchCommand.
 */
public class ReaderAllBooksCommand implements ActionCommand {

    /**
     * Reference to book copy DAO.
     */
    private final BookCopyDao bookCopyDao;

    /**
     * Constructor for reader command.
     */
    public ReaderAllBooksCommand() {
        this.bookCopyDao = new BookCopyDao(DataSourceHolder.getDataSource());
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
            request.setAttribute("booksCopies", bookCopyDao.getAll());

        return ConfigurationManager.getProperty("path.page.reader");
    }
}
