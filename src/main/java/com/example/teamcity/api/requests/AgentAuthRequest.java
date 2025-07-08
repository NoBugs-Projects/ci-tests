package com.example.teamcity.api.requests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;

import java.util.Objects;

/**
 * Specialized request class for managing TeamCity agent authorization.
 * Provides operations to change agent authorization state.
 *
 * @author TeamCity Testing Framework
 * @since 1.0
 */
public final class AgentAuthRequest {
    
    /** The URL endpoint for agent authorization */
    private static final String AGENT_AUTH_URL = "/app/rest/agents/name:%s/authorized";
    
    /** The request specification containing authentication and headers */
    private final RequestSpecification spec;

    /**
     * Constructs a new AgentAuthRequest instance.
     *
     * @param spec the request specification containing authentication and headers
     * @throws IllegalArgumentException if spec is null
     */
    public AgentAuthRequest(RequestSpecification spec) {
        this.spec = Objects.requireNonNull(spec, "RequestSpecification cannot be null");
    }

    /**
     * Changes the authorization state of an agent.
     *
     * @param name the name of the agent
     * @param state the new authorization state (true for authorized, false for unauthorized)
     * @throws IllegalArgumentException if name is null or empty
     * @throws RuntimeException if the request fails
     */
    public void changeState(String name, Boolean state) {
        Objects.requireNonNull(name, "Agent name cannot be null");
        Objects.requireNonNull(state, "State cannot be null");
        
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("Agent name cannot be empty");
        }
        
        RestAssured.given()
                .spec(spec)
                .accept(ContentType.ANY)
                .contentType(ContentType.TEXT)
                .body(state)
                .put(AGENT_AUTH_URL.formatted(name))
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body(Matchers.containsString("true"));
    }
} 