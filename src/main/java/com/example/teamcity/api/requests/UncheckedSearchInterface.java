package com.example.teamcity.api.requests;

import io.restassured.response.Response;

/**
 * Interface defining unchecked search operations for API entities.
 * Unchecked operations return raw HTTP responses without automatic status code validation.
 *
 * @author TeamCity Testing Framework
 * @since 1.0
 */
public interface UncheckedSearchInterface {
    
    /**
     * Retrieves all entities of the specified type.
     *
     * @param attribute the attribute to filter by (can be null for all entities)
     * @return the raw HTTP response
     * @throws IllegalArgumentException if attribute is invalid
     */
    Response findAll(String attribute);

    /**
     * Searches for entities based on the provided search parameter.
     * The search parameter should be in the format "field:value".
     *
     * @param searchParameter the search parameter in format "field:value"
     * @return the raw HTTP response
     * @throws IllegalArgumentException if searchParameter is null, empty, or malformed
     */
    Response search(String searchParameter);
} 