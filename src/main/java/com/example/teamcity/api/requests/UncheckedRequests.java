package com.example.teamcity.api.requests;

import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import io.restassured.specification.RequestSpecification;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * Factory class for creating and managing unchecked API requests.
 * Provides access to unchecked request implementations for all endpoints.
 * Unchecked requests return raw HTTP responses without automatic status code validation.
 *
 * @author TeamCity Testing Framework
 * @since 1.0
 */
public final class UncheckedRequests {
    
    /** Map of endpoint to unchecked request implementation */
    private final Map<Endpoint, UncheckedBase> requests;

    /**
     * Constructs a new UncheckedRequests instance for the given specification.
     * Initializes unchecked request implementations for all available endpoints.
     *
     * @param spec the request specification containing authentication and headers
     * @throws IllegalArgumentException if spec is null
     */
    public UncheckedRequests(RequestSpecification spec) {
        Objects.requireNonNull(spec, "RequestSpecification cannot be null");
        
        this.requests = new EnumMap<>(Endpoint.class);
        for (Endpoint endpoint : Endpoint.values()) {
            requests.put(endpoint, new UncheckedBase(spec, endpoint));
        }
    }

    /**
     * Gets an unchecked request for the specified endpoint.
     *
     * @param endpoint the endpoint to get a request for
     * @return an unchecked request implementation for the specified endpoint
     * @throws IllegalArgumentException if endpoint is null
     */
    public UncheckedBase getRequest(Endpoint endpoint) {
        Objects.requireNonNull(endpoint, "Endpoint cannot be null");
        return requests.get(endpoint);
    }

    /**
     * Gets all available unchecked requests.
     *
     * @return an unmodifiable map of all endpoint to request mappings
     */
    public Map<Endpoint, UncheckedBase> getAllRequests() {
        return Map.copyOf(requests);
    }
}