package edu.udemy.micronaut.controller;

import edu.udemy.micronaut.constants.Constants;
import edu.udemy.micronaut.constants.Errors;
import edu.udemy.micronaut.controller.dto.DepositFiatMoney;
import edu.udemy.micronaut.controller.dto.Symbol;
import edu.udemy.micronaut.controller.dto.Wallet;
import edu.udemy.micronaut.controller.dto.error.CustomError;
import edu.udemy.micronaut.controller.dto.inmuable.InMemoryAccountStore;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientException;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.json.tree.JsonNode;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@MicronautTest
class WalletControllerTest {

    @Inject
    @Client("/accounts/wallets")
    private HttpClient client;

    @Inject
    private InMemoryAccountStore inMemoryAccountStore;


    @BeforeEach
    public void init() {
        inMemoryAccountStore.deleteWatchList(Constants.ACCOUNT_ID);
        inMemoryAccountStore.deleteWallet(Constants.ACCOUNT_ID);
    }

    @Test
    void getWallet() {
        final JsonNode response = client.toBlocking().retrieve("/", JsonNode.class);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(0, response.size());
    }

    @Test
    void depositMoney() {

        final DepositFiatMoney depositFiatMoney =
                new DepositFiatMoney(Constants.ACCOUNT_ID, UUID.randomUUID(), new Symbol("EUR"), BigDecimal.TEN);

        final var response =
                client.toBlocking().exchange(HttpRequest.POST("/deposits", depositFiatMoney), Wallet.class);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(200, response.getStatus().getCode());
        Assertions.assertEquals(depositFiatMoney.walletId(), response.getBody().get().walletId());
    }

    @Test
    void depositMoney_ko() {

        final DepositFiatMoney depositFiatMoney =
                new DepositFiatMoney(Constants.ACCOUNT_ID, UUID.randomUUID(), new Symbol("ABC"), BigDecimal.TEN);

        try {
            client.toBlocking().exchange(HttpRequest.POST("/deposits", depositFiatMoney), CustomError.class);
        } catch (HttpClientException e) {
            Optional<CustomError> response = ((HttpClientResponseException) e).getResponse().getBody(CustomError.class);
            Assertions.assertTrue(response.isPresent());
            Assertions.assertEquals(400, response.get().status());
            Assertions.assertEquals(Errors.UNSUPPORTED_FIAT_CURRENTY.getError(), response.get().error());
        }

    }

    @Test
    void withdrawMoney() {

        final DepositFiatMoney depositFiatMoney =
                new DepositFiatMoney(Constants.ACCOUNT_ID, UUID.randomUUID(), new Symbol("EUR"), new BigDecimal("-100"));

        final var response =
                client.toBlocking().exchange(HttpRequest.POST("/withdraws", depositFiatMoney), Wallet.class);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(200, response.getStatus().getCode());
        Assertions.assertEquals(depositFiatMoney.walletId(), response.getBody().get().walletId());
    }

    @Test
    void withdrawMoney_ko() {

        final DepositFiatMoney depositFiatMoney =
                new DepositFiatMoney(Constants.ACCOUNT_ID, UUID.randomUUID(), new Symbol("ABC"), new BigDecimal("-100"));

        try {
            client.toBlocking().exchange(HttpRequest.POST("/withdraws", depositFiatMoney), CustomError.class);
        } catch (HttpClientException e) {
            Optional<CustomError> response = ((HttpClientResponseException) e).getResponse().getBody(CustomError.class);
            Assertions.assertTrue(response.isPresent());
            Assertions.assertEquals(400, response.get().status());
            Assertions.assertEquals(Errors.UNSUPPORTED_FIAT_CURRENTY.getError(), response.get().error());
        }

    }

}
