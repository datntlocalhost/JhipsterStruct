package jp.co.run.web.rest.vm;

import javax.validation.constraints.Size;

import jp.co.run.common.constants.MessageConstants;
import jp.co.run.common.validations.annotation.Require;

/**
 * View Model object for storing a user's credentials.
 */
public class LoginVM {

    @Require(message = MessageConstants.NOT_NULL_MSG)
    @Size(min = 1, max = 50)
    private String username;

    @Require(message = MessageConstants.NOT_NULL_MSG)
    private String password;

    private Boolean rememberMe;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    @Override
    public String toString() {
        return "LoginVM{" +
            "username='" + username + '\'' +
            ", rememberMe=" + rememberMe +
            '}';
    }
}
