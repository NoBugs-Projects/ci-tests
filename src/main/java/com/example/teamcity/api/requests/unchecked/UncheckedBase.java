package com.example.teamcity.api.requests.unchecked;

import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.models.BaseModel;
import com.example.teamcity.api.requests.Request;
import com.example.teamcity.api.requests.UncheckedCrudInterface;
import com.example.teamcity.api.requests.UncheckedSearchInterface;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Objects;

/**
 * Unchecked implementation of API requests that returns raw HTTP responses.
 * This class provides CRUD and search operations without automatic status code validation.
 * Users are responsible for handling HTTP status codes and error conditions.
 *
 * @author TeamCity Testing Framework
 * @since 1.0
 */
public class UncheckedBase extends Request implements UncheckedCrudInterface, UncheckedSearchInterface {

    /**
     * Constructs a new UncheckedBase instance for the specified endpoint.
     *
     * @param spec the request specification containing authentication and headers
     * @param endpoint the API endpoint this request operates on
     * @throws IllegalArgumentException if spec or endpoint is null
     */
    public UncheckedBase(RequestSpecification spec, Endpoint endpoint) {
        super(spec, endpoint);
    }

    @Override
    @Step("Creating entity")
    public Response create(BaseModel model) {
        Objects.requireNonNull(model, "Model cannot be null");
        
        return RestAssured
                .given()
                .spec(spec)
                .body(model)
                .post(endpoint.getUrl());
    }

    @Override
    @Step("Performing findAll operation with attribute={attribute}")
    public Response findAll(String attribute) {
        return RestAssured
                .given()
                .spec(spec)
                .get(endpoint.getUrl());
    }

    @Override
    @Step("Performing read operation with locator={locator}")
    public Response read(String locator) {
        Objects.requireNonNull(locator, "Locator cannot be null");
        if (locator.trim().isEmpty()) {
            throw new IllegalArgumentException("Locator cannot be empty");
        }
        
        return RestAssured
                .given()
                .spec(spec)
                .get(endpoint.getUrl() + "/" + locator);
    }

    @Override
    @Step("Updating entity with locator={locator}")
    public Response update(String locator, BaseModel model) {
        Objects.requireNonNull(locator, "Locator cannot be null");
        Objects.requireNonNull(model, "Model cannot be null");
        if (locator.trim().isEmpty()) {
            throw new IllegalArgumentException("Locator cannot be empty");
        }
        
        return RestAssured
                .given()
                .body(model)
                .spec(spec)
                .put(endpoint.getUrl() + "/" + locator);
    }

    @Override
    @Step("Deleting entity with locator={locator}")
    public Response delete(String locator) {
        Objects.requireNonNull(locator, "Locator cannot be null");
        if (locator.trim().isEmpty()) {
            throw new IllegalArgumentException("Locator cannot be empty");
        }
        
        return RestAssured
                .given()
                .spec(spec)
                .delete(endpoint.getUrl() + "/" + locator);
    }

    @Override
    @Step("Performing search with searchParameter={searchParameter}")
    public Response search(String searchParameter) {
        Objects.requireNonNull(searchParameter, "Search parameter cannot be null");
        if (searchParameter.trim().isEmpty()) {
            throw new IllegalArgumentException("Search parameter cannot be empty");
        }
        
        var params = searchParameter.split(":");
        if (params.length != 2) {
            throw new IllegalArgumentException("Search parameter must be in format 'field:value'");
        }
        
        return RestAssured
                .given()
                .spec(spec)
                .pathParams(params[0], params[1])
                .get("%s/{%s}".formatted(endpoint.getUrl(), params[0]));
    }
}