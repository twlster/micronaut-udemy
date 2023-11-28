package edu.udemy.micronaut.controller.dto.error.handler;

import edu.udemy.micronaut.controller.dto.error.CustomError;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.server.exceptions.response.ErrorContext;
import io.micronaut.http.server.exceptions.response.ErrorResponseProcessor;
import jakarta.inject.Singleton;

@Singleton
public class CustomErrorResponseProcessor implements ErrorResponseProcessor<CustomError> {
    @Override
    public @NonNull MutableHttpResponse<CustomError> processResponse(@NonNull ErrorContext errorContext, @NonNull MutableHttpResponse<?> baseResponse) {
        CustomError customError = null;
        if (!errorContext.hasErrors()) {
            customError = new CustomError(
                    baseResponse.getStatus().getCode(),
                    baseResponse.getStatus().name(),
                    "No custom error found...");
        } else {

            customError = new CustomError(
                    baseResponse.getStatus().getCode(),
                    baseResponse.getStatus().name(),
                    errorContext.getErrors().get(0).getMessage());
        }
        return baseResponse.body(customError).contentType(MediaType.APPLICATION_JSON);
    }
}
