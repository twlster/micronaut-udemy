package edu.udemy.micronaut.controller;

import edu.udemy.micronaut.constants.Constants;
import edu.udemy.micronaut.controller.dto.Symbol;
import edu.udemy.micronaut.controller.dto.WatchList;
import edu.udemy.micronaut.controller.dto.inmuable.InMemoryAccountStore;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.security.token.render.BearerAccessRefreshToken;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

@MicronautTest
class WatchlistControllerTest implements ILogin {

    @Inject
    @Client("/")
    private HttpClient client;

    @Inject
    private InMemoryAccountStore inMemoryAccountStore;

    private BearerAccessRefreshToken token;

    @BeforeEach
    public void init() {
        inMemoryAccountStore.deleteWatchList(Constants.ACCOUNT_ID);
        token = login(client);
    }

    @Test
    void no_WatchList() {
        var request = HttpRequest.GET("/accounts/watchlist").bearerAuth(token.getAccessToken());
        final WatchList response = client.toBlocking().retrieve(request, WatchList.class);
        Assertions.assertNull(response.symbols());
    }

    @Test
    void no_WatchList_ko() {
        try {
            var request = HttpRequest.GET("/accounts/watchlist");
            client.toBlocking().retrieve(request, WatchList.class);
        } catch(HttpClientResponseException e){
            Assertions.assertEquals(HttpStatus.UNAUTHORIZED.getReason(), e.getMessage());
        }
    }

    @Test
    void getWatchList() {
        inMemoryAccountStore.updateWatchList(Constants.ACCOUNT_ID,
                new WatchList(Stream.of("123", "456", "789").map(Symbol::new).toList()));
        var request = HttpRequest.GET("/accounts/watchlist").bearerAuth(token.getAccessToken());
        var response = client.toBlocking().exchange(request, WatchList.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertEquals(3, response.getBody().get().symbols().size());
    }

    @Test
    void putWatchList() {

        WatchList watchList = new WatchList(Stream.of("123", "456", "789").map(Symbol::new).toList());

        var request = HttpRequest.PUT("/accounts/watchlist", watchList).bearerAuth(token.getAccessToken());
        var response = client.toBlocking().exchange(request, WatchList.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertEquals(3, response.getBody().get().symbols().size());
    }

    @Test
    void deleteWatchList() {
        inMemoryAccountStore.updateWatchList(Constants.ACCOUNT_ID,
                new WatchList(Stream.of("123", "456", "789").map(Symbol::new).toList()));
        var request = HttpRequest.DELETE("/accounts/watchlist").bearerAuth(token.getAccessToken());
        var response = client.toBlocking().exchange(request);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
    }
}
