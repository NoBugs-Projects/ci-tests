package com.example.teamcity.api.requests.checked;

import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.generators.TestDataStorage;
import com.example.teamcity.api.models.BaseModel;
import com.example.teamcity.api.requests.CrudInterface;
import com.example.teamcity.api.requests.Request;
import com.example.teamcity.api.requests.SearchInterface;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

import java.util.List;
import java.util.Objects;

/**
 * Checked implementation of API requests that automatically validates HTTP status codes.
 * This class provides type-safe CRUD and search operations with automatic error handling.
 * All operations throw exceptions if the HTTP status code indicates an error.
 *
 * @param <T> the type of entity this request operates on, must extend BaseModel
 * @author TeamCity Testing Framework
 * @since 1.0
 */
@SuppressWarnings("unchecked")
public final class CheckedBase<T extends BaseModel> extends Request implements CrudInterface<T>, SearchInterface<T> {
    
    /** The unchecked base implementation used for making actual HTTP requests */
    private final UncheckedBase uncheckedBase;

    /**
     * Constructs a new CheckedBase instance for the specified endpoint.
     *
     * @param spec the request specification containing authentication and headers
     * @param endpoint the API endpoint this request operates on
     * @throws IllegalArgumentException if spec or endpoint is null
     */
    public CheckedBase(RequestSpecification spec, Endpoint endpoint) {
        super(spec, endpoint);
        this.uncheckedBase = new UncheckedBase(spec, endpoint);
    }

    @Override
    public List<T> findAll(String attribute) {
        return (List<T>) uncheckedBase
                .findAll(attribute)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .jsonPath()
                .getList(attribute, endpoint.getModelClass());
    }

    @Override
    public T create(T model) {
        Objects.requireNonNull(model, "Model cannot be null");
        
        var createdModel = (T) uncheckedBase
                .create(model)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(endpoint.getModelClass());

        TestDataStorage.getStorage().addCreatedEntity(endpoint, createdModel);
        return createdModel;
    }

    @Override
    public T read(String id) {
        Objects.requireNonNull(id, "ID cannot be null");
        if (id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be empty");
        }
        
        return (T) uncheckedBase
                .read(id)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(endpoint.getModelClass());
    }

    @Override
    public T update(String id, T model) {
        Objects.requireNonNull(id, "ID cannot be null");
        Objects.requireNonNull(model, "Model cannot be null");
        if (id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be empty");
        }
        
        return (T) uncheckedBase
                .update(id, model)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(endpoint.getModelClass());
    }

    @Override
    public Object delete(String id) {
        Objects.requireNonNull(id, "ID cannot be null");
        if (id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be empty");
        }
        
        return uncheckedBase
                .delete(id)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .asString();
    }

    @Override
    public T search(String searchParameter) {
        Objects.requireNonNull(searchParameter, "Search parameter cannot be null");
        if (searchParameter.trim().isEmpty()) {
            throw new IllegalArgumentException("Search parameter cannot be empty");
        }
        
        return (T) uncheckedBase
                .search(searchParameter)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(endpoint.getModelClass());
    }
}