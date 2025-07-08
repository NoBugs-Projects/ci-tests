package com.example.teamcity.api.requests;

import com.example.teamcity.api.models.ServerAuthSettings;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

import java.util.Objects;

/**
 * Specialized request class for managing TeamCity server authentication settings.
 * Provides operations to read and update server authentication configuration.
 *
 * @author TeamCity Testing Framework
 * @since 1.0
 */
public final class ServerAuthRequest {
    
    /** The URL endpoint for server authentication settings */
    private static final String SERVER_AUTH_SETTINGS_URL = "/app/rest/server/authSettings";
    
    /** The request specification containing authentication and headers */
    private final RequestSpecification spec;

    /**
     * Constructs a new ServerAuthRequest instance.
     *
     * @param spec the request specification containing authentication and headers
     * @throws IllegalArgumentException if spec is null
     */
    public ServerAuthRequest(RequestSpecification spec) {
        this.spec = Objects.requireNonNull(spec, "RequestSpecification cannot be null");
    }

    /**
     * Retrieves the current server authentication settings.
     *
     * @return the current server authentication settings
     * @throws RuntimeException if the request fails
     */
    public ServerAuthSettings read() {
        return RestAssured.given()
                .spec(spec)
                .get(SERVER_AUTH_SETTINGS_URL)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(ServerAuthSettings.class);
    }

    /**
     * Updates the server authentication settings.
     *
     * @param authSettings the new authentication settings to apply
     * @return the updated authentication settings
     * @throws IllegalArgumentException if authSettings is null
     * @throws RuntimeException if the request fails
     */
    public ServerAuthSettings update(ServerAuthSettings authSettings) {
        Objects.requireNonNull(authSettings, "AuthSettings cannot be null");
        
        return RestAssured.given()
                .spec(spec)
                .body(authSettings)
                .put(SERVER_AUTH_SETTINGS_URL)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(ServerAuthSettings.class);
    }
}
