package by.pvt.safronenko.library.beans;

/**
 * Author.
 */
public class Author {

    /**
     * Primary key.
     */
    private Long id;

    /**
     * First name.
     */
    private String firstName;

    /**
     * Middle name.
     */
    private String middleName;

    /**
     * Last name.
     */
    private String lastName;

    /**
     * Default constructor.
     */
    public Author() {
    }

    /**
     * Constructs the author with given id.
     *
     * @param id Id.
     */
    public Author(Long id) {
        this.id = id;
    }

    /**
     * Returns id.
     *
     * @return id.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id id.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns firstName.
     *
     * @return firstName.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets firstName.
     *
     * @param firstName firstName.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns middleName.
     *
     * @return middleName.
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * Sets middleName.
     *
     * @param middleName middleName.
     */
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    /**
     * Returns lastName.
     *
     * @return lastName.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets lastName.
     *
     * @param lastName lastName.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns full name of the author, including middle name if present.
     *
     * @return Full name.
     */
    public String getFullName() {
        return firstName + ' ' +
                (middleName != null ? middleName : "") +
                ' ' + lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Author author = (Author) o;
        return id != null ? id.equals(author.id) : author.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
