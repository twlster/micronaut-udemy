package edu.udemy.micronaut.controller;

import edu.udemy.micronaut.controller.dto.Symbol;
import edu.udemy.micronaut.controller.dto.inmuable.InMemoryStore;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.json.tree.JsonNode;
import io.micronaut.security.token.render.BearerAccessRefreshToken;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@MicronautTest
class SymbolControllerTest implements ILogin{

    @Inject
    @Client("/")
    private  HttpClient client;

    @Inject
    private InMemoryStore inMemoryStore;

    private BearerAccessRefreshToken token;

    @BeforeEach
    public void init(){
        inMemoryStore.init(10);
        token = login(client);
    }

    @Test
    void symbols() {
        var request = HttpRequest.GET("/symbols").bearerAuth(token.getAccessToken());
        var response = client.toBlocking().exchange(request, JsonNode.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertEquals(10, response.getBody().get().size());
    }

    @Test
    void symbols_ko() {
        try {
            var request = HttpRequest.GET("/symbols");
            client.toBlocking().exchange(request, JsonNode.class);
        } catch(HttpClientResponseException e){
            Assertions.assertEquals(HttpStatus.UNAUTHORIZED.getReason(), e.getMessage());
        }
    }

    @Test
    void symbol() {
        Symbol firstSymbol = new Symbol("TEST");
        inMemoryStore.getSymbols().put(firstSymbol.value(), firstSymbol);

        var request = HttpRequest.GET("/symbols/"+firstSymbol.value()).bearerAuth(token.getAccessToken());
        var response = client.toBlocking().exchange(request, Symbol.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertEquals(firstSymbol.value(), response.getBody().get().value());
    }

    @Test
    void filter() {
        var request = HttpRequest.GET("/symbols/filter?max=5&offset=7").bearerAuth(token.getAccessToken());
        var response = client.toBlocking().exchange(request, JsonNode.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertEquals(3, response.getBody().get().size());
    }

    @Test
    void filter_max() {
        var request = HttpRequest.GET("/symbols/filter?max=5").bearerAuth(token.getAccessToken());
        var response = client.toBlocking().exchange(request, JsonNode.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertEquals(5, response.getBody().get().size());
    }

    @Test
    void filter_offset() {
        var request = HttpRequest.GET("/symbols/filter?offset=7").bearerAuth(token.getAccessToken());
        var response = client.toBlocking().exchange(request, JsonNode.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertEquals(3, response.getBody().get().size());
    }

}
