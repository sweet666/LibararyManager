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
public class ReaderBookSearchCommand implements ActionCommand {

    /**
     * Reference to book copy DAO.
     */
    private final BookCopyDao bookCopyDao;

    /**
     * Constructor for reader command.
     */
    public ReaderBookSearchCommand() {
        this.bookCopyDao = new BookCopyDao(DataSourceHolder.getDataSource());
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String query = StringUtils.trimToEmpty(request.getParameter("q"));
        if (!query.isEmpty()) {
            request.setAttribute("booksCopies", bookCopyDao.search(query));
            request.setAttribute("query", query);
        }
        else {
            request.setAttribute("booksCopies", bookCopyDao.getAll());
        }

        return ConfigurationManager.getProperty("path.page.reader");
    }
}
