package com.example.teamcity.api.requests;

import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.models.BaseModel;
import com.example.teamcity.api.requests.checked.CheckedBase;
import io.restassured.specification.RequestSpecification;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * Factory class for creating and managing checked API requests.
 * Provides type-safe access to checked request implementations for all endpoints.
 * Checked requests automatically validate HTTP status codes and throw exceptions on errors.
 *
 * @author TeamCity Testing Framework
 * @since 1.0
 */
public final class CheckedRequests {
    
    /** Map of endpoint to checked request implementation */
    private final Map<Endpoint, CheckedBase<?>> requests;

    /**
     * Constructs a new CheckedRequests instance for the given specification.
     * Initializes checked request implementations for all available endpoints.
     *
     * @param spec the request specification containing authentication and headers
     * @throws IllegalArgumentException if spec is null
     */
    public CheckedRequests(RequestSpecification spec) {
        Objects.requireNonNull(spec, "RequestSpecification cannot be null");
        
        this.requests = new EnumMap<>(Endpoint.class);
        for (Endpoint endpoint : Endpoint.values()) {
            requests.put(endpoint, new CheckedBase<>(spec, endpoint));
        }
    }

    /**
     * Gets a type-safe checked request for the specified endpoint.
     *
     * @param <T> the type of entity this request operates on
     * @param endpoint the endpoint to get a request for
     * @return a checked request implementation for the specified endpoint
     * @throws IllegalArgumentException if endpoint is null
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseModel> CheckedBase<T> getRequest(Endpoint endpoint) {
        Objects.requireNonNull(endpoint, "Endpoint cannot be null");
        return (CheckedBase<T>) requests.get(endpoint);
    }

    /**
     * Gets all available checked requests.
     *
     * @return an unmodifiable map of all endpoint to request mappings
     */
    public Map<Endpoint, CheckedBase<?>> getAllRequests() {
        return Map.copyOf(requests);
    }
}