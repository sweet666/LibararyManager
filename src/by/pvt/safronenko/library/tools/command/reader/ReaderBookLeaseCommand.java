package by.pvt.safronenko.library.tools.command.reader;

import by.pvt.safronenko.library.tools.dao.BookCopyDao;
import by.pvt.safronenko.library.beans.BookCopy;
import by.pvt.safronenko.library.tools.StringUtils;
import by.pvt.safronenko.library.tools.command.ActionCommand;
import by.pvt.safronenko.library.tools.datasource.DataSourceHolder;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ReaderBookLeaseCommand.
 */
public class ReaderBookLeaseCommand implements ActionCommand {

	/**
	 * Log4j logger (with logback implementation).
	 */
	private static final Logger logger = Logger.getLogger(ReaderBookLeaseCommand.class);

	/**
	 * Reference to book copy DAO.
	 */
	private final BookCopyDao bookCopyDao;

	/**
	 * Constructor for reader command.
	 */
	public ReaderBookLeaseCommand() {
		this.bookCopyDao = new BookCopyDao(DataSourceHolder.getDataSource());
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		if (request.getParameter("retainSerialNumber") != null) {
			String retainSerialNumber = request.getParameter("retainSerialNumber");
			BookCopy bookCopy = bookCopyDao.get(retainSerialNumber);
			if (bookCopy != null) {
				String user = (String) request.getSession().getAttribute("loggedInUser");
				if ("true".equals(request.getParameter("inRoom"))) {
					bookCopy.bookInRoom(user);
				} else
					bookCopy.book(user);
				bookCopyDao.update(bookCopy);
				logger.warn(String.format("Book copy %s %s [%s].", retainSerialNumber, bookCopy.getState(),
						request.getRemoteAddr()));
			}
		}

		// Redirect to original servlet with query.
		String query = StringUtils.trimToEmpty(request.getParameter("q"));
		return redirectTo(response, "reader_book_search&q=" + urlEncode(query));
	}
}
