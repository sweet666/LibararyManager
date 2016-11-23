package by.pvt.safronenko.library.tools.dao;

import by.pvt.safronenko.library.beans.Author;
import by.pvt.safronenko.library.tools.StringUtils;
import by.pvt.safronenko.library.tools.datasource.DataSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author DAO.
 */
public class AuthorDao {

    /**
     * Reference to base DAO object.
     */
    private DataSource dataSource;

    /**
     * Constructs author service.
     *
     * @param dataSource Base DAO.
     */
    public AuthorDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Returns all authors sorted in alphabet order.
     *
     * @return All authors sorted in alphabet order.
     * @throws RuntimeException In case of exception.
     */
    public List<Author> getAll() {
        try {
            List<Map<String, Object>> resultSets = dataSource.getAll("authors", "lastName,firstName");
            List<Author> authors = new ArrayList<>(resultSets.size());
            for (Map<String, Object> resultSet : resultSets) {
                authors.add(toAuthor(resultSet));
            }
            return authors;
        } catch (SQLException e) {
            throw new RuntimeException("Error while getting authors.", e);
        }
    }

    /**
     * Returns author by given id.
     *
     * @param id Author id.
     * @return Author with given id.
     * @throws RuntimeException In case of exception.
     */
    public Author get(long id) {
        try {
            return toAuthor(dataSource.get("authors", id));
        } catch (SQLException e) {
            throw new RuntimeException("Error while getting author.", e);
        }
    }

    /**
     * Removes author by given id.
     *
     * @param id Author id.
     * @return Author with given id.
     * @throws RuntimeException In case of exception.
     */
    public boolean remove(long id) {
        try {
            return dataSource.remove("authors", id);
        } catch (SQLException e) {
            throw new RuntimeException("Error while removing author.", e);
        }
    }

    /**
     * Creates author by given parameters.
     *
     * @param firstName First name.
     * @param lastName  Last name.
     * @return Author.
     * @throws RuntimeException In case of exception.
     */
    public Author create(String firstName, String middleName, String lastName) {
        firstName = StringUtils.capitalize(firstName);
        lastName = StringUtils.capitalize(lastName);
        // Nullable field.
        if (middleName != null) {
            middleName = StringUtils.capitalize(middleName);
        }

        if (firstName.isEmpty() || lastName.isEmpty()) {
            throw new IllegalArgumentException("Please, provide first and last name.");
        }

        Map<String, Object> authorData = new HashMap<>(3);
        authorData.put("firstName", firstName);
        authorData.put("lastName", lastName);
        authorData.put("middleName", middleName);

        try {
            long id = dataSource.insert("authors", authorData);
            Author author = new Author();
            author.setFirstName(firstName);
            author.setLastName(lastName);
            author.setMiddleName(middleName);
            author.setId(id);
            return author;
        } catch (SQLException e) {
            throw new RuntimeException("Error while creating author.", e);
        }
    }

    /**
     * Updates given author.
     *
     * @param author Author.
     * @return Whether the author was updated.
     * @throws RuntimeException In case of exception.
     */
    public boolean update(Author author) {
        String firstName = StringUtils.capitalize(author.getFirstName());
        String lastName = StringUtils.capitalize(author.getLastName());
        String middleName = author.getMiddleName();
        if (middleName != null) {
            middleName = StringUtils.capitalize(middleName);
        }

        if (firstName.isEmpty() || lastName.isEmpty()) {
            throw new IllegalArgumentException("Please, provide first and last name.");
        }

        Map<String, Object> authorData = new HashMap<>(4);
        authorData.put("firstName", firstName);
        authorData.put("lastName", lastName);
        authorData.put("middleName", middleName);
        authorData.put("id", author.getId());

        try {
            return dataSource.update("authors", authorData);
        } catch (SQLException e) {
            throw new RuntimeException("Error while creating author.", e);
        }
    }

    /**
     * Constructs author from given result set.
     *
     * @param resultSet Result set.
     * @return Author.
     */
    private Author toAuthor(Map<String, Object> resultSet) {
        if (resultSet == null) {
            return null;
        }
        Author author = new Author();
        author.setId((Long) resultSet.get("id"));
        author.setFirstName((String) resultSet.get("firstName"));
        author.setLastName((String) resultSet.get("lastName"));
        Object middleNameAsObject = resultSet.get("middleName");
        // Nullable field.
        if (middleNameAsObject != null) {
            author.setMiddleName((String) middleNameAsObject);
        }
        return author;
    }
}
