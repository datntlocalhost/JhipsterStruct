package jp.co.run.service;

import jp.co.run.service.dto.UserSubDto;

/**
 * The Interface UserService.
 */
public interface UserService {
    
    /**
     * Gets the user by username.
     *
     * @param username the username
     * @return the user by username
     */
    UserSubDto getUserByUsername(String username);
}
