package com.example.teamcity.api.requests;

import com.example.teamcity.api.models.BaseModel;
import io.restassured.response.Response;

/**
 * Interface defining unchecked CRUD operations for API entities.
 * Unchecked operations return raw HTTP responses without automatic status code validation.
 *
 * @author TeamCity Testing Framework
 * @since 1.0
 */
public interface UncheckedCrudInterface {
    
    /**
     * Creates a new entity on the server.
     *
     * @param model the entity to create
     * @return the raw HTTP response
     * @throws IllegalArgumentException if model is null
     */
    Response create(BaseModel model);

    /**
     * Retrieves an entity by its identifier.
     *
     * @param id the unique identifier of the entity
     * @return the raw HTTP response
     * @throws IllegalArgumentException if id is null or empty
     */
    Response read(String id);

    /**
     * Updates an existing entity on the server.
     *
     * @param id the unique identifier of the entity to update
     * @param model the updated entity data
     * @return the raw HTTP response
     * @throws IllegalArgumentException if id or model is null
     */
    Response update(String id, BaseModel model);

    /**
     * Deletes an entity from the server.
     *
     * @param id the unique identifier of the entity to delete
     * @return the raw HTTP response
     * @throws IllegalArgumentException if id is null or empty
     */
    Response delete(String id);
} 