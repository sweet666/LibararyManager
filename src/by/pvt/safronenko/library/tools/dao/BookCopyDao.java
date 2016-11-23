package by.pvt.safronenko.library.tools.dao;

import by.pvt.safronenko.library.beans.Author;
import by.pvt.safronenko.library.beans.Book;
import by.pvt.safronenko.library.beans.BookCopy;
import by.pvt.safronenko.library.enums.BookCopyState;
import by.pvt.safronenko.library.tools.datasource.DataSource;

import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Book copy DAO.
 */
public class BookCopyDao {

    /**
     * Pattern for book copy SQL query.
     */
    private static final String BOOK_COPY_SELECT_PATTERN =
            "SELECT bc.*, " +
                    "b.title, b.edition, b.year, " +
                    "a.firstName, a.lastName, a.middleName, a.id as author_id " +
                    "FROM booksCopies bc " +
                    "LEFT JOIN books b ON b.isbn = bc.isbn " +
                    "LEFT JOIN authors a ON a.id = b.author_id " +
                    "WHERE %s " +
                    "%s " +
                    "ORDER BY %s";

    /**
     * Select all copies query.
     */
    private static final String BOOK_COPY_SELECT_ALL =
            String.format(BOOK_COPY_SELECT_PATTERN, true, "", "bc.lastStateChange ASC");

    /**
     * Select one book copy query.
     */
    private static final String BOOK_COPY_SELECT_ONE =
            String.format(BOOK_COPY_SELECT_PATTERN, "bc.serialNumber = ?", "", "bc.lastStateChange ASC");

    /**
     * Search query.
     */
    private static final String BOOK_COPY_SEARCH =
            String.format(BOOK_COPY_SELECT_PATTERN,
                    "b.title like ? OR a.firstName like ? OR a.lastName like ?",
                    "",
                    "b.title, bc.bookCopyState");

    /**
     * Reference to entity manager.
     */
    private DataSource dataSource;

    /**
     * Constructs author service.
     *
     * @param dataSource Entity manager.
     */
    public BookCopyDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Searches for books by given query.
     *
     * @param query Query to search.
     * @return List of books that matches query.
     */
    public List<BookCopy> search(String query) {
        query = '%' + query.trim().replace('%', ' ') + '%';
        try {
            // This query may produces two book copies per book: AVAILABLE and BOOKED.
            // So, simply ignore the repeating book: this excludes BOOKED book copies if result set has AVAILABLE book.
            List<Map<String, Object>> resultSets = dataSource.getAllWithSql(BOOK_COPY_SEARCH, query, query, query);
            BookCopy previous = null;
            List<BookCopy> booksCopies = new ArrayList<>(resultSets.size());
            for (Map<String, Object> resultSet : resultSets) {
                BookCopy bookCopy = toBookCopy(resultSet);
                if (previous == null || !bookCopy.getBook().equals(previous.getBook())) {
                    booksCopies.add(bookCopy);
                }
                previous = bookCopy;
            }
            return booksCopies;
        } catch (SQLException e) {
            throw new RuntimeException("Error while getting books.", e);
        }
    }

    /**
     * Returns all books' copies ordered by last state change date time.
     *
     * @return all books' copies ordered by last state change date time.
     */
    public List<BookCopy> getAll() {
        try {
            List<Map<String, Object>> resultSets = dataSource.getAllWithSql(BOOK_COPY_SELECT_ALL);
            List<BookCopy> booksCopies = new ArrayList<>(resultSets.size());
            for (Map<String, Object> resultSet : resultSets) {
                booksCopies.add(toBookCopy(resultSet));
            }
            return booksCopies;
        } catch (SQLException e) {
            throw new RuntimeException("Error while getting books.", e);
        }
    }

    /**
     * Returns one book copy by serial number.
     *
     * @param serialNumber Serial number.
     * @return Book copy or null.
     */
    public BookCopy get(String serialNumber) {
        try {
            List<Map<String, Object>> resultSets = dataSource.getAllWithSql(BOOK_COPY_SELECT_ONE, serialNumber);
            List<BookCopy> bookCopies = new ArrayList<>(resultSets.size());
            for (Map<String, Object> resultSet : resultSets) {
                bookCopies.add(toBookCopy(resultSet));
            }
            if (bookCopies.isEmpty()) {
                return null;
            }
            return bookCopies.get(0);
        } catch (SQLException e) {
            throw new RuntimeException("Error while getting book copy: " + serialNumber, e);
        }
    }

    /**
     * Creates a book copy.
     *
     * @param bookCopy Book copy.
     */
    public void create(BookCopy bookCopy) {
        try {
            dataSource.insert("booksCopies", toMap(bookCopy));
        } catch (SQLException e) {
            throw new RuntimeException("Error while creating book copy.", e);
        }
    }

    /**
     * Updates book copy data.
     *
     * @param bookCopy Book copy.
     * @return Whether the book copy was updated.
     */
    public boolean update(BookCopy bookCopy) {
        try {
            return dataSource.update("booksCopies", "serialNumber", toMap(bookCopy));
        } catch (SQLException e) {
            throw new RuntimeException("Error while creating book copy.", e);
        }
    }

    /**
     * Removes book copy by given serial number.
     *
     * @param serialNumber Serial number.
     * @return Whether the book copy was removed.
     */
    public boolean remove(String serialNumber) {
        try {
            return dataSource.remove("booksCopies", "serialNumber", serialNumber);
        } catch (SQLException e) {
            throw new RuntimeException("Error while removing book copy.", e);
        }
    }

    /**
     * Constructs book copy object from result set.
     *
     * @param resultSet Result set.
     * @return Book copy.
     */
    private BookCopy toBookCopy(Map<String, Object> resultSet) {
        if (resultSet == null || resultSet.isEmpty()) {
            return null;
        }

        // These fields are expected to be present in result set (authors).
        Author author = new Author((long) resultSet.get("author_id"));
        author.setFirstName((String) resultSet.get("firstName"));
        author.setLastName((String) resultSet.get("lastName"));
        author.setMiddleName((String) resultSet.get("middleName"));

        // Book from joined data.
        Book book = new Book();
        book.setAuthor(author);
        book.setIsbn((String) resultSet.get("isbn"));
        book.setTitle((String) resultSet.get("title"));
        book.setYear((Integer) resultSet.get("year"));

        // Book copy.
        BookCopy bookCopy = new BookCopy();
        bookCopy.setBook(book);
        bookCopy.setSerialNumber((String) resultSet.get("serialNumber"));
        bookCopy.setUsername((String) resultSet.get("username"));
        bookCopy.setState(BookCopyState.valueOf((String) resultSet.get("bookCopyState")));
        ZonedDateTime lastStateChange = ZonedDateTime.ofInstant(
                Instant.ofEpochSecond((long) resultSet.get("lastStateChange")),
                ZoneId.of("Europe/Minsk"));
        bookCopy.setLastStateChange(lastStateChange);

        return bookCopy;
    }

    /**
     * Constructs result set map from given book.
     *
     * @param bookCopy Book copy.
     * @return Result set map.
     */
    private Map<String, Object> toMap(BookCopy bookCopy) {
        Map<String, Object> bookCopyData = new HashMap<>();
        bookCopyData.put("serialNumber", bookCopy.getSerialNumber());
        bookCopyData.put("isbn", bookCopy.getBook().getIsbn());
        bookCopyData.put("username", bookCopy.getUsername());
        bookCopyData.put("bookCopyState", bookCopy.getState().toString());
        bookCopyData.put("lastStateChange", bookCopy.getLastStateChange().toEpochSecond());
        return bookCopyData;
    }
}
