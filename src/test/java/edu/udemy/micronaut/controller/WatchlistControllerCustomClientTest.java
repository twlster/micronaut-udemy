package edu.udemy.micronaut.controller;

import edu.udemy.micronaut.client.JWTWatchListClient;
import edu.udemy.micronaut.constants.Constants;
import edu.udemy.micronaut.controller.dto.Symbol;
import edu.udemy.micronaut.controller.dto.WatchList;
import edu.udemy.micronaut.controller.dto.inmuable.InMemoryAccountStore;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.render.BearerAccessRefreshToken;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

@MicronautTest
class WatchlistControllerCustomClientTest {

    @Inject
    @Client("/")
    private JWTWatchListClient client;

    @Inject
    private InMemoryAccountStore inMemoryAccountStore;

    private String token;

    @BeforeEach
    public void init() {
        inMemoryAccountStore.deleteWatchList(Constants.ACCOUNT_ID);
        token = this.login();
    }

    @Test
    void no_WatchList() {
        final WatchList response = client.getWatchlist(token).getBody().get();
        Assertions.assertNull(response.symbols());
    }

    @Test
    void no_WatchList_ko() {
        try {
            token = "";
            client.getWatchlist(token);
        } catch(HttpClientResponseException e){
            Assertions.assertEquals(HttpStatus.UNAUTHORIZED.getReason(), e.getMessage());
        }
    }

    @Test
    void getWatchList() {
        inMemoryAccountStore.updateWatchList(Constants.ACCOUNT_ID,
                new WatchList(Stream.of("123", "456", "789").map(Symbol::new).toList()));
        var response = client.getWatchlistAsASingleList(token);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertEquals(3, response.getBody().get().symbols().size());
    }

    @Test
    void putWatchList() {

        WatchList watchList = new WatchList(Stream.of("123", "456", "789").map(Symbol::new).toList());

        var response = client.updateWatchlist(token, watchList);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertEquals(3, response.getBody().get().symbols().size());
    }

    @Test
    void deleteWatchList() {
        inMemoryAccountStore.updateWatchList(Constants.ACCOUNT_ID,
                new WatchList(Stream.of("123", "456", "789").map(Symbol::new).toList()));
        var response = client.deleteWatchlist(token);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
    }

    public String login(){
        final UsernamePasswordCredentials usernamePasswordCredentials = new UsernamePasswordCredentials(Constants.USERNAME, Constants.PASSWORD);
        BearerAccessRefreshToken token = client.login(usernamePasswordCredentials);
        return "Bearer ".concat(token.getAccessToken());
    }
}
