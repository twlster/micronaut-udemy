package edu.udemy.micronaut.controller;

import edu.udemy.micronaut.constants.Constants;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.render.BearerAccessRefreshToken;
import org.junit.jupiter.api.Assertions;

public interface ILogin {

    default BearerAccessRefreshToken login(HttpClient client) {
        final UsernamePasswordCredentials credentials =
                new UsernamePasswordCredentials(Constants.USERNAME,Constants.PASSWORD);

        var login = HttpRequest.POST("/login", credentials);
        var loginResponse = client.toBlocking().exchange(login, BearerAccessRefreshToken.class);

        Assertions.assertEquals(HttpStatus.OK, loginResponse.getStatus());
        Assertions.assertEquals(Constants.USERNAME, loginResponse.getBody().get().getUsername());

        return loginResponse.getBody().get();
    }
}
