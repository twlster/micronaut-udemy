package edu.udemy.micronaut.controller.dto.error.handler;

import edu.udemy.micronaut.constants.Errors;
import edu.udemy.micronaut.controller.dto.error.CustomError;
import edu.udemy.micronaut.controller.dto.error.FiatCurrencyNotSupportedException;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Produces
@Singleton
@Requires(classes = {FiatCurrencyNotSupportedException.class, Exception.class})
public class FiatCurrencyNotSupportedExceptionHandler implements ExceptionHandler<FiatCurrencyNotSupportedException, HttpResponse<CustomError>> {

    @Override
    public HttpResponse<CustomError> handle(HttpRequest request, FiatCurrencyNotSupportedException exception) {
        return HttpResponse.badRequest(
                new CustomError(
                        HttpStatus.BAD_REQUEST.getCode(),
                        Errors.UNSUPPORTED_FIAT_CURRENTY.getError(),
                        exception.getMessage())
        );
    }
}
