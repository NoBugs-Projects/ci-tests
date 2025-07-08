package com.example.teamcity.api.requests;

import com.example.teamcity.api.enums.Endpoint;
import io.restassured.specification.RequestSpecification;

import java.util.Objects;

/**
 * Base class for all API requests in the TeamCity testing framework.
 * Provides common functionality for request specification and endpoint management.
 *
 * @author TeamCity Testing Framework
 * @since 1.0
 */
public abstract class Request {
    
    /** The request specification containing authentication and common headers */
    protected final RequestSpecification spec;
    
    /** The API endpoint this request operates on */
    protected final Endpoint endpoint;

    /**
     * Constructs a new Request with the specified specification and endpoint.
     *
     * @param spec the request specification containing authentication and headers
     * @param endpoint the API endpoint this request operates on
     * @throws IllegalArgumentException if spec or endpoint is null
     */
    public Request(RequestSpecification spec, Endpoint endpoint) {
        this.spec = Objects.requireNonNull(spec, "RequestSpecification cannot be null");
        this.endpoint = Objects.requireNonNull(endpoint, "Endpoint cannot be null");
    }

    /**
     * Gets the request specification used by this request.
     *
     * @return the request specification
     */
    public RequestSpecification getSpec() {
        return spec;
    }

    /**
     * Gets the endpoint this request operates on.
     *
     * @return the endpoint
     */
    public Endpoint getEndpoint() {
        return endpoint;
    }
}