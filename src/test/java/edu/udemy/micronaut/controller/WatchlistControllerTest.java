package edu.udemy.micronaut.controller;

import edu.udemy.micronaut.constants.Constants;
import edu.udemy.micronaut.controller.dto.Symbol;
import edu.udemy.micronaut.controller.dto.WatchList;
import edu.udemy.micronaut.controller.dto.inmuable.InMemoryAccountStore;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.stream.Stream;

@MicronautTest
class WatchlistControllerTest {

    @Inject
    @Client("/accounts/watchlist")
    private HttpClient client;

    @Inject
    private InMemoryAccountStore inMemoryAccountStore;

    @BeforeEach
    public void init() {
        inMemoryAccountStore.deleteWatchList(Constants.ACCOUNT_ID);
    }

    @Test
    void no_WatchList() {
        final WatchList response = client.toBlocking().retrieve("/", WatchList.class);
        Assertions.assertNull(response.symbols());
    }

    @Test
    void getWatchList() {
        inMemoryAccountStore.updateWatchList(Constants.ACCOUNT_ID,
                new WatchList(Stream.of("123", "456", "789").map(Symbol::new).toList()));
        var response = client.toBlocking().exchange("/", WatchList.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertEquals(3, response.getBody().get().symbols().size());
    }

    @Test
    void putWatchList() {

        WatchList watchList = new WatchList(Stream.of("123", "456", "789").map(Symbol::new).toList());

        var response = client.toBlocking().exchange(HttpRequest.PUT("/", watchList), WatchList.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertEquals(3, response.getBody().get().symbols().size());
    }

    @Test
    void deleteWatchList() {
        inMemoryAccountStore.updateWatchList(Constants.ACCOUNT_ID,
                new WatchList(Stream.of("123", "456", "789").map(Symbol::new).toList()));
        var response = client.toBlocking().exchange(HttpRequest.DELETE("/"));
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
    }
}
