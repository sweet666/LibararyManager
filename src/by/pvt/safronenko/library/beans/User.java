package by.pvt.safronenko.library.beans;

import by.pvt.safronenko.library.enums.UserRole;

import java.time.ZonedDateTime;

/**
 * User.
 */
public class User {

    /**
     * User id: primary key.
     */
    private Long id;

    /**
     * Username.
     */
    private String username;

    /**
     * Email.
     */
    private String email;

    /**
     * Password hash.
     */
    private String passwordHash;

    /**
     * Password salt.
     */
    private String passwordSalt;

    /**
     * The last time of the state change.
     */
    private ZonedDateTime lastLogin;

    /**
     * User role.
     */
    private UserRole role;

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
     * Returns email.
     *
     * @return email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns password hash.
     *
     * @return password hash.
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Sets password hash.
     *
     * @param passwordHash password hash.
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Returns password salt.
     *
     * @return password salt.
     */
    public String getPasswordSalt() {
        return passwordSalt;
    }

    /**
     * Sets passwordSalt.
     *
     * @param passwordSalt passwordSalt.
     */
    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    /**
     * Returns lastLogin.
     *
     * @return lastLogin.
     */
    public ZonedDateTime getLastLogin() {
        return lastLogin;
    }

    /**
     * Sets lastLogin.
     *
     * @param lastLogin lastLogin.
     */
    public void setLastLogin(ZonedDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    /**
     * Returns role.
     *
     * @return role.
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * Sets role.
     *
     * @param role role.
     */
    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id != null ? id.equals(user.id) : user.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
