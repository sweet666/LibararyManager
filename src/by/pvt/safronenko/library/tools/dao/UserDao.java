package by.pvt.safronenko.library.tools.dao;

import by.pvt.safronenko.library.beans.User;
import by.pvt.safronenko.library.exceptions.UserCreationException;
import by.pvt.safronenko.library.enums.UserRole;
import by.pvt.safronenko.library.tools.datasource.DataSource;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * UserDao.
 */
public class UserDao {

    /**
     * Reference to data source.
     */
    private DataSource dataSource;

    /**
     * Base64 encoder.
     */
    private Base64.Encoder encoder;

    /**
     * Constructs author service.
     *
     * @param dataSource Data source.
     */
    public UserDao(DataSource dataSource) {
        this.dataSource = dataSource;
        this.encoder = Base64.getEncoder().withoutPadding();
    }

    /**
     * Gets user by id.
     *
     * @param userId Id.
     * @return User or null if there is no such user.
     */
    public User get(long userId) {
        try {
            return toUser(dataSource.get("users", userId));
        } catch (SQLException e) {
            throw new RuntimeException("Error while getting user..", e);
        }
    }

    /**
     * Returns user by given email or username if password matches hash in database. Otherwise returns null.
     *
     * @param emailOrUsername Email or username.
     * @param password        Password.
     * @return User by given email or username/password or null if not found or password doesn't match.
     */
    public User signin(String emailOrUsername, String password) {
        User existing = null;
        if (emailOrUsername.contains("@")) {
            existing = getUserByEmail(emailOrUsername);
        } else {
            existing = getUserByUsername(emailOrUsername);
        }
        if (existing == null) {
            return null;
        }
        String hash = hash(existing.getPasswordSalt(), password);

        // Check hash.
        if (hash.equals(existing.getPasswordHash())) {
            return existing;
        }
        return null;
    }

    /**
     * Returns user by email.
     *
     * @param email Email.
     * @return User by email or null if not found.
     */
    public User getUserByEmail(String email) {
        try {
            return toUser(dataSource.getWithCondition("users", "email = ?", email));
        } catch (SQLException e) {
            throw new RuntimeException("Error while getting user.", e);
        }
    }

    /**
     * Returns user by username.
     *
     * @param username Username.
     * @return User by username or null if not found.
     */
    public User getUserByUsername(String username) {
        try {
            return toUser(dataSource.getWithCondition("users", "username = ?", username));
        } catch (SQLException e) {
            throw new RuntimeException("Error while getting user.", e);
        }
    }

    /**
     * Creates user with given username, email and password.
     *
     * @param username Username.
     * @param email    Email.
     * @param password Password.
     * @param role     User role.
     * @return Created user.
     * @throws UserCreationException when user already exists or problem happens.
     */
    public User createUser(String username, String email, String password, UserRole role) throws UserCreationException {
        email = email.trim().toLowerCase();
        username = username.trim().toLowerCase();
        if (email.isEmpty() || username.isEmpty()) {
            throw new UserCreationException("Username and email are required.");
        }
        User userByEmail = getUserByEmail(email);
        if (userByEmail != null) {
            throw new UserCreationException("User with email " + email + " already exists");
        }
        User userByUsername = getUserByUsername(username);
        if (userByUsername != null) {
            throw new UserCreationException("User with username " + username + " already exists");
        }
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setRole(role);
        user.setLastLogin(ZonedDateTime.now(ZoneId.of("Europe/Minsk")));

        // Store salt and hash, not real password.
        String salt = UUID.randomUUID().toString();
        String hash = hash(salt, password);
        user.setPasswordSalt(salt);
        user.setPasswordHash(hash);

        try {
            Long userId = dataSource.insert("users", toMap(user));
            user.setId(userId);
            return user;
        } catch (SQLException e) {
            throw new UserCreationException("Error while creating a user.", e);
        }
    }

    /**
     * Transforms user to data set map.
     *
     * @param user User.
     * @return Data set map.
     */
    private Map<String, Object> toMap(User user) {
        Map<String, Object> userData = new HashMap<>();
        if (user.getId() != null) {
            userData.put("id", user.getId());
        }
        userData.put("email", user.getEmail());
        userData.put("username", user.getUsername());
        userData.put("passwordHash", user.getPasswordHash());
        userData.put("passwordSalt", user.getPasswordSalt());
        userData.put("role", user.getRole().toString());
        userData.put("lastLogin", user.getLastLogin().toEpochSecond());
        return userData;
    }

    /**
     * Converts result set to user.
     *
     * @param resultSet Result set.
     * @return User or null.
     */
    private User toUser(Map<String, Object> resultSet) {
        if (resultSet == null || resultSet.isEmpty()) {
            return null;
        }

        User user = new User();
        if (resultSet.get("id") != null) {
            user.setId((long) resultSet.get("id"));
        }
        user.setUsername((String) resultSet.get("username"));
        user.setEmail((String) resultSet.get("email"));
        user.setPasswordHash((String) resultSet.get("passwordHash"));
        user.setPasswordSalt((String) resultSet.get("passwordSalt"));
        user.setRole(UserRole.valueOf((String) resultSet.get("role")));
        user.setLastLogin(ZonedDateTime.ofInstant(
                Instant.ofEpochSecond((long) resultSet.get("lastLogin")),
                ZoneId.of("Europe/Minsk")));
        return user;
    }

    /**
     * Calculates HMAC SHA-256 hash, encodes it with Base64.
     *
     * @param salt     Salt.
     * @param password Password.
     * @return Hash from the password.
     */
    public String hash(String salt, String password) {
        try {
            Mac sha256HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(salt.getBytes(), "HmacSHA256");
            sha256HMAC.init(secret_key);
            return encoder.encodeToString(sha256HMAC.doFinal(password.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
            throw new RuntimeException("Password hash exception.", e);
        }
    }
}
