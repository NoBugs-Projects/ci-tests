package com.example.teamcity.api.generators;

import com.example.teamcity.api.annotations.Optional;
import com.example.teamcity.api.annotations.Parameterizable;
import com.example.teamcity.api.annotations.Random;
import com.example.teamcity.api.models.BaseModel;
import com.example.teamcity.api.models.TestData;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class TestDataGenerator {

    private TestDataGenerator() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * The main method for generating test data.
     *
     * @param generatedModels List of previously generated models to reuse
     * @param generatorClass Class to generate instance of
     * @param parameters Parameters for parameterizable fields
     * @return Generated instance
     * @throws IllegalArgumentException if generatorClass is null
     */
    public static <T extends BaseModel> T generate(List<BaseModel> generatedModels, Class<T> generatorClass,
                                                   Object... parameters) {
        validateInput(generatorClass);

        try {
            T instance = createInstance(generatorClass);
            populateFields(instance, generatedModels, parameters);
            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                 | NoSuchMethodException e) {
            throw new IllegalStateException("Cannot generate test data for class: " + generatorClass.getSimpleName(), e);
        }
    }

    private static <T extends BaseModel> void validateInput(Class<T> generatorClass) {
        if (generatorClass == null) {
            throw new IllegalArgumentException("Generator class cannot be null");
        }
        if (!BaseModel.class.isAssignableFrom(generatorClass)) {
            throw new IllegalArgumentException("Generator class must extend BaseModel");
        }
    }

    private static <T extends BaseModel> T createInstance(Class<T> generatorClass)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return generatorClass.getDeclaredConstructor().newInstance();
    }

    private static <T extends BaseModel> void populateFields(T instance, List<BaseModel> generatedModels,
                                                             Object... parameters) throws IllegalAccessException {
        Field[] fields = instance.getClass().getDeclaredFields();
        int parameterIndex = 0;

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (!field.isAnnotationPresent(Optional.class)) {
                    parameterIndex = populateField(instance, field, generatedModels, parameters, parameterIndex);
                }
            } finally {
                field.setAccessible(false);
            }
        }
    }

    private static <T extends BaseModel> int populateField(T instance, Field field, List<BaseModel> generatedModels,
                                                           Object[] parameters, int parameterIndex) throws IllegalAccessException {
        if (field.isAnnotationPresent(Parameterizable.class) && parameterIndex < parameters.length) {
            field.set(instance, parameters[parameterIndex]);
            return parameterIndex + 1;
        } else if (field.isAnnotationPresent(Random.class)) {
            setRandomValue(instance, field);
        } else if (BaseModel.class.isAssignableFrom(field.getType())) {
            setBaseModelValue(instance, field, generatedModels, parameters);
        } else if (List.class.isAssignableFrom(field.getType())) {
            setListValue(instance, field, generatedModels, parameters);
        }
        return parameterIndex;
    }

    private static <T extends BaseModel> void setRandomValue(T instance, Field field) throws IllegalAccessException {
        if (String.class.equals(field.getType())) {
            field.set(instance, RandomData.getString());
        }
        // Add support for other primitive types if needed
    }

    private static <T extends BaseModel> void setBaseModelValue(T instance, Field field,
                                                                List<BaseModel> generatedModels, Object[] parameters)
            throws IllegalAccessException {
        java.util.Optional<BaseModel> existingModel = findExistingModel(generatedModels, field.getType());
        BaseModel value = existingModel.orElseGet(() ->
                generate(generatedModels, field.getType().asSubclass(BaseModel.class), parameters));
        field.set(instance, value);
    }

    private static <T extends BaseModel> void setListValue(T instance, Field field,
                                                           List<BaseModel> generatedModels, Object[] parameters)
            throws IllegalAccessException {
        if (field.getGenericType() instanceof ParameterizedType parameterizedType) {
            Class<?> typeClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
            if (BaseModel.class.isAssignableFrom(typeClass)) {
                java.util.Optional<BaseModel> existingModel = findExistingModel(generatedModels, typeClass);
                List<BaseModel> value = existingModel.map(List::of)
                        .orElseGet(() -> List.of(generate(generatedModels, typeClass.asSubclass(BaseModel.class), parameters)));
                field.set(instance, value);
            }
        }
    }

    private static java.util.Optional<BaseModel> findExistingModel(List<BaseModel> generatedModels, Class<?> targetType) {
        return generatedModels.stream()
                .filter(model -> model.getClass().equals(targetType))
                .findFirst();
    }

    public static <T extends BaseModel> T generate(Class<T> generatorClass, Object... parameters) {
        return generate(Collections.emptyList(), generatorClass, parameters);
    }

    public static TestData generate() {
        try {
            TestData instance = TestData.class.getDeclaredConstructor().newInstance();
            List<BaseModel> generatedModels = new ArrayList<>();

            for (Field field : TestData.class.getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    if (BaseModel.class.isAssignableFrom(field.getType())) {
                        BaseModel generatedModel = generate(generatedModels, field.getType().asSubclass(BaseModel.class));
                        field.set(instance, generatedModel);
                        generatedModels.add(generatedModel);
                    }
                } finally {
                    field.setAccessible(false);
                }
            }
            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new IllegalStateException("Cannot generate TestData", e);
        }
    }
}