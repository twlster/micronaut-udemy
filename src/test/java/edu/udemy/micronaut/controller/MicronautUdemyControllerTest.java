package edu.udemy.micronaut.controller;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.json.tree.JsonNode;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@MicronautTest
class MicronautUdemyControllerTest {

    @Inject
    @Client("/micronaut-udemy")
    HttpClient client;

    @Test
    void testItWorks() {
        var response = client.toBlocking().exchange("/", String.class);
        Assertions.assertEquals("Example Response", response.getBody().orElse(""));
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
    }

    @Test
    void translations() {
        var response = client.toBlocking().exchange("/translations", JsonNode.class);
        Assertions.assertEquals("{es=Buenos días, en=Good Morning, de=Guten Morgen}",
                response.getBody().get().getValue().toString());
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
    }

}
