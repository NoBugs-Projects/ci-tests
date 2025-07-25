package com.example.teamcity.api.requests;

import com.example.teamcity.api.models.BaseModel;

import java.util.List;

/**
 * Interface defining search operations for API entities.
 * Provides type-safe search operations for entities that extend BaseModel.
 *
 * @param <T> the type of entity this interface operates on, must extend BaseModel
 * @author TeamCity Testing Framework
 * @since 1.0
 */
public interface SearchInterface<T extends BaseModel> {
    
    /**
     * Retrieves all entities of the specified type.
     *
     * @param attribute the attribute to filter by (can be null for all entities)
     * @return a list of entities matching the criteria
     * @throws IllegalArgumentException if attribute is invalid
     */
    List<T> findAll(String attribute);

    /**
     * Searches for entities based on the provided search parameter.
     * The search parameter should be in the format "field:value".
     *
     * @param searchParameter the search parameter in format "field:value"
     * @return the found entity or null if not found
     * @throws IllegalArgumentException if searchParameter is null, empty, or malformed
     */
    T search(String searchParameter);
}
