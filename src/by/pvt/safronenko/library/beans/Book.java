package by.pvt.safronenko.library.beans;


/**
 * Book entity.
 */
public class Book {

    /**
     * ISBN for the book: unique identifier.
     */
    private String isbn;

    /**
     * Book title.
     */
    private String title;

    /**
     * Book description.
     */
    private String description;

    /**
     * Book year.
     */
    private int year;

    /**
     * Edition.
     */
    private int edition;

    /**
     * Author.
     */
    private Author author;

    /**
     * Default constructor.
     */
    public Book() {
    }

    /**
     * Constructs empty book with ISBN.
     *
     * @param isbn ISBN.
     */
    public Book(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Returns ISBN.
     *
     * @return ISBN.
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Sets isbn.
     *
     * @param isbn isbn.
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Returns title.
     *
     * @return title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns description.
     *
     * @return description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns year.
     *
     * @return year.
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets year.
     *
     * @param year year.
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Returns edition.
     *
     * @return edition.
     */
    public int getEdition() {
        return edition;
    }

    /**
     * Sets edition.
     *
     * @param edition edition.
     */
    public void setEdition(int edition) {
        this.edition = edition;
    }

    /**
     * Returns author.
     *
     * @return author.
     */
    public Author getAuthor() {
        return author;
    }

    /**
     * Sets author.
     *
     * @param author author.
     */
    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        return isbn.equals(book.isbn);

    }

    @Override
    public int hashCode() {
        return isbn.hashCode();
    }
}
