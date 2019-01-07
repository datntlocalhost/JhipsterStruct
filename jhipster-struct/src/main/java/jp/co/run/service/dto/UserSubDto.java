package jp.co.run.service.dto;

/**
 * The Class UserSubDto.
 */
public class UserSubDto {
    
    /** The login. */
    private String login;
    
    /** The email. */
    private String email;
    
    /**
     * Instantiates a new user sub dto.
     *
     * @param login the login
     * @param email the email
     */
    public UserSubDto(String login, String email) {
        this.login = login;
        this.email = email;
    }

    /**
     * Gets the login.
     *
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * Sets the login.
     *
     * @param login the new login
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Gets the email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email.
     *
     * @param email the new email
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
