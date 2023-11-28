package edu.udemy.micronaut.controller;

import edu.udemy.micronaut.controller.dto.Symbol;
import edu.udemy.micronaut.controller.dto.inmuable.InMemoryStore;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.json.tree.JsonNode;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@MicronautTest
class SymbolControllerTest {

    @Inject
    @Client("/symbols")
    private HttpClient client;

    @Inject
    private InMemoryStore inMemoryStore;

    @BeforeEach
    public void init(){
        inMemoryStore.init(10);
    }

    @Test
    void symbols() {
        var response = client.toBlocking().exchange("/", JsonNode.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertEquals(10, response.getBody().get().size());
    }

    @Test
    void symbol() {
        Symbol firstSymbol = new Symbol("TEST");
        inMemoryStore.getSymbols().put(firstSymbol.value(), firstSymbol);
        var response = client.toBlocking().exchange("/"+firstSymbol.value(), Symbol.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertEquals(firstSymbol.value(), response.getBody().get().value());
    }

    @Test
    void filter() {
        var response = client.toBlocking().exchange("/filter?max=5&offset=7", JsonNode.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertEquals(3, response.getBody().get().size());
    }

    @Test
    void filter_max() {
        var response = client.toBlocking().exchange("/filter?max=5", JsonNode.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertEquals(5, response.getBody().get().size());
    }

    @Test
    void filter_offset() {
        var response = client.toBlocking().exchange("/filter?offset=7", JsonNode.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertEquals(3, response.getBody().get().size());
    }

}
