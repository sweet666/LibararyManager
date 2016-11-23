package by.pvt.safronenko.library.tools.dao;

import by.pvt.safronenko.library.beans.Author;
import by.pvt.safronenko.library.beans.Book;
import by.pvt.safronenko.library.tools.datasource.DataSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Book DAO.
 */
public class BookDao {

    /**
     * Pattern for book SQL query.
     */
    private static final String BOOK_SELECT_PATTERN =
            "SELECT b.*, a.firstName, a.lastName, a.middleName, a.id as author_id " +
                    "FROM books b " +
                    "LEFT JOIN authors a ON a.id = b.author_id " +
                    "WHERE %s " +
                    "ORDER BY b.title, b.edition";

    /**
     * Select all query.
     */
    private static final String BOOK_SELECT_ALL = String.format(BOOK_SELECT_PATTERN, "true");

    /**
     * Select one query.
     */
    private static final String BOOK_SELECT_ONE = String.format(BOOK_SELECT_PATTERN, "b.isbn = ?");

    /**
     * Reference to data source.
     */
    private DataSource dataSource;

    /**
     * Constructs author service.
     *
     * @param dataSource Data source.
     */
    public BookDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Returns all books ordered by title and edition.
     *
     * @return all books ordered by title and edition.
     */
    public List<Book> getAll() {
        try {
            List<Map<String, Object>> resultSets = dataSource.getAllWithSql(BOOK_SELECT_ALL);
            List<Book> books = new ArrayList<>(resultSets.size());
            for (Map<String, Object> resultSet : resultSets) {
                books.add(toBook(resultSet));
            }
            return books;
        } catch (SQLException e) {
            throw new RuntimeException("Error while getting books.", e);
        }
    }

    /**
     * Returns one book by ISBN.
     *
     * @param isbn ISBN.
     * @return Book or null.
     */
    public Book get(String isbn) {
        try {
            List<Map<String, Object>> resultSets = dataSource.getAllWithSql(BOOK_SELECT_ONE, isbn);
            List<Book> books = new ArrayList<>(resultSets.size());
            for (Map<String, Object> resultSet : resultSets) {
                books.add(toBook(resultSet));
            }
            if (books.isEmpty()) {
                return null;
            }
            return books.get(0);
        } catch (SQLException e) {
            throw new RuntimeException("Error while getting book: " + isbn, e);
        }
    }

    /**
     * Creates a book.
     *
     * @param book Book.
     */
    public void create(Book book) {
        try {
            dataSource.insert("books", toMap(book));
        } catch (SQLException e) {
            throw new RuntimeException("Error while creating book.", e);
        }
    }

    /**
     * Updates book data.
     *
     * @param book Book.
     * @return Whether the book was updated.
     */
    public boolean update(Book book) {
        try {
            return dataSource.update("books", "isbn", toMap(book));
        } catch (SQLException e) {
            throw new RuntimeException("Error while creating book.", e);
        }
    }

    /**
     * Removes book by given ISBN.
     *
     * @param isbn ISBN.
     * @return Whether the book was removed.
     */
    public boolean remove(String isbn) {
        try {
            return dataSource.remove("books", "isbn", isbn);
        } catch (SQLException e) {
            throw new RuntimeException("Error while removing book.", e);
        }
    }

    /**
     * Constructs book from given result set.
     *
     * @param resultSet Result set.
     * @return Book or null if result set is null.
     */
    private Book toBook(Map<String, Object> resultSet) {
        if (resultSet == null || resultSet.isEmpty()) {
            return null;
        }
        // These fields are expected to be present in result set (authors).
        Author author = new Author((long) resultSet.get("author_id"));
        author.setFirstName((String) resultSet.get("firstName"));
        author.setLastName((String) resultSet.get("lastName"));
        author.setMiddleName((String) resultSet.get("middleName"));

        Book book = new Book();
        book.setIsbn((String) resultSet.get("isbn"));
        book.setTitle((String) resultSet.get("title"));
        book.setDescription((String) resultSet.get("description"));
        book.setYear((int) resultSet.get("year"));
        book.setEdition((int) resultSet.get("edition"));

        book.setAuthor(author);
        return book;
    }

    /**
     * Constructs result set map from given book.
     *
     * @param book Book.
     * @return Result set map.
     */
    private Map<String, Object> toMap(Book book) {
        Map<String, Object> bookData = new HashMap<>();
        bookData.put("isbn", book.getIsbn());
        bookData.put("title", book.getTitle());
        bookData.put("author_id", book.getAuthor().getId());
        bookData.put("description", book.getDescription());
        bookData.put("year", book.getYear());
        bookData.put("edition", book.getEdition());
        return bookData;
    }
}
