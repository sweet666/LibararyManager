package by.pvt.safronenko.library.beans;

import by.pvt.safronenko.library.enums.BookCopyState;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Book copy.
 */
public class BookCopy {

    /**
     * Serial number of a book copy.
     */
    private String serialNumber;

    /**
     * Reference to the book.
     */
    private Book book;

    /**
     * State.
     */
    private BookCopyState state;

    /**
     * Username who booked the book copy.
     */
    private String username;

    /**
     * The last time of the state change.
     */
    private ZonedDateTime lastStateChange;

    /**
     * Default constructor.
     */
    public BookCopy() {
        this.state = BookCopyState.AVAILABLE;
        this.lastStateChange = ZonedDateTime.now(ZoneId.of("Europe/Minsk"));
    }

    /**
     * Constructor with serial number.
     *
     * @param serialNumber Serial number.
     */
    public BookCopy(String serialNumber) {
        this();
        this.serialNumber = serialNumber.trim();
    }

    /**
     * Returns serialNumber.
     *
     * @return serialNumber.
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * Sets serialNumber.
     *
     * @param serialNumber serialNumber.
     */
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * Returns book.
     *
     * @return book.
     */
    public Book getBook() {
        return book;
    }

    /**
     * Sets book.
     *
     * @param book book.
     */
    public void setBook(Book book) {
        this.book = book;
    }

    /**
     * Returns state.
     *
     * @return state.
     */
    public BookCopyState getState() {
        return state;
    }

    /**
     * Sets state.
     *
     * @param state state.
     */
    public void setState(BookCopyState state) {
        this.state = state;
    }

    /**
     * Returns username.
     *
     * @return username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns lastStateChange.
     *
     * @return lastStateChange.
     */
    public ZonedDateTime getLastStateChange() {
        return lastStateChange;
    }

    /**
     * Sets lastStateChange.
     *
     * @param lastStateChange lastStateChange.
     */
    public void setLastStateChange(ZonedDateTime lastStateChange) {
        this.lastStateChange = lastStateChange;
    }

    /**
     * Books current book copy by given user.
     *
     * @param username Username to book the book copy.
     */
    public void book(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username must not be empty.");
        }
        this.username = username;
        this.state = BookCopyState.BOOKED;
        this.lastStateChange = ZonedDateTime.now(ZoneId.of("Europe/Minsk"));
    }

    /**
     * Books current book copy by given user in room.
     *
     * @param username Username to book the book copy in room.
     */
    public void bookInRoom(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username must not be empty.");
        }
        this.username = username;
        this.state = BookCopyState.INROOM;
        this.lastStateChange = ZonedDateTime.now(ZoneId.of("Europe/Minsk"));
    }

    /**
     * Releases current book copy.
     */
    public void release() {
        this.username = null;
        this.state = BookCopyState.AVAILABLE;
        this.lastStateChange = ZonedDateTime.now(ZoneId.of("Europe/Minsk"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookCopy bookCopy = (BookCopy) o;
        return serialNumber.equals(bookCopy.serialNumber);

    }

    @Override
    public int hashCode() {
        return serialNumber.hashCode();
    }
}
