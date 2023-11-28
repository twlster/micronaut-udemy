package edu.udemy.micronaut.controller.dto.error;

import edu.udemy.micronaut.controller.dto.common.RestApiResponse;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record CustomError(int status, String error, String message) implements RestApiResponse {
}
