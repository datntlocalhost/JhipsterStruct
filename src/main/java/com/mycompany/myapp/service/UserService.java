package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.User;

/**
 * Service Interface for managing User.
 */
public interface UserService {

    /**
     * Save a user.
     *
     * @param user the entity to save
     * @return the persisted entity
     */
    User save(User user);

    /**
     * Gets the user by id.
     *
     * @param id the id
     * @return the user by id
     */
    User getUserById(Long id);
    
    /**
     * Delete the "id" user.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
