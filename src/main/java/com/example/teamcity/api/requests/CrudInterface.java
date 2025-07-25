package com.example.teamcity.api.requests;

import com.example.teamcity.api.models.BaseModel;

/**
 * Interface defining CRUD (Create, Read, Update, Delete) operations for API entities.
 * Provides type-safe operations for managing entities that extend BaseModel.
 *
 * @param <T> the type of entity this interface operates on, must extend BaseModel
 * @author TeamCity Testing Framework
 * @since 1.0
 */
public interface CrudInterface<T extends BaseModel> {
    
    /**
     * Creates a new entity on the server.
     *
     * @param model the entity to create
     * @return the created entity with server-generated fields populated
     * @throws IllegalArgumentException if model is null
     */
    T create(T model);

    /**
     * Retrieves an entity by its identifier.
     *
     * @param id the unique identifier of the entity
     * @return the found entity
     * @throws IllegalArgumentException if id is null or empty
     */
    T read(String id);

    /**
     * Updates an existing entity on the server.
     *
     * @param id the unique identifier of the entity to update
     * @param model the updated entity data
     * @return the updated entity
     * @throws IllegalArgumentException if id or model is null
     */
    T update(String id, T model);

    /**
     * Deletes an entity from the server.
     *
     * @param id the unique identifier of the entity to delete
     * @return the response from the delete operation
     * @throws IllegalArgumentException if id is null or empty
     */
    Object delete(String id);
}
