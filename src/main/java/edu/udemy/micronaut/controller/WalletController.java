package edu.udemy.micronaut.controller;

import edu.udemy.micronaut.constants.Constants;
import edu.udemy.micronaut.constants.Errors;
import edu.udemy.micronaut.controller.dto.DepositFiatMoney;
import edu.udemy.micronaut.controller.dto.Wallet;
import edu.udemy.micronaut.controller.dto.WithdrawFiatMoney;
import edu.udemy.micronaut.controller.dto.common.RestApiResponse;
import edu.udemy.micronaut.controller.dto.error.CustomError;
import edu.udemy.micronaut.controller.dto.error.FiatCurrencyNotSupportedException;
import edu.udemy.micronaut.controller.dto.inmuable.InMemoryAccountStore;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

@Controller("/accounts/wallets")
@Tag(name = "Accounts Wallet Api")
@Secured(SecurityRule.IS_AUTHENTICATED)
public record WalletController(InMemoryAccountStore accountStore) {

    private static final Logger LOG = LoggerFactory.getLogger(WalletController.class);

    public static final List<String> SUPPORTED_FIAT_CURRENCIES = List.of("EUR", "USD", "CHF", "GBP");

    @Get(uri = "${micronaut.rest.api.wallet.url}",
            produces = MediaType.APPLICATION_JSON)
    @Operation(summary = "Account Wallet",
            description = "Returns the wallet of an account"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            array = @ArraySchema(items = @Schema(implementation = Wallet.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid data Supplied",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = CustomError.class))}),
            @ApiResponse(responseCode = "404", description = "Wallet not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = CustomError.class))})
    })
    public Collection<Wallet> getWallets() {
        LOG.debug("Recovering the wallet of an account");
        return accountStore.getWallets(Constants.ACCOUNT_ID);
    }

    @Post(uri = "${micronaut.rest.api.wallet.url.deposits}",
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON)
    @Operation(summary = "Wallet Deposit",
            description = "Makes a deposit to an account wallet"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = Wallet.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data Supplied",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = CustomError.class))}),
            @ApiResponse(responseCode = "404", description = "Wallet not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = CustomError.class))})
    })
    public HttpResponse<RestApiResponse> depositMoney(@Body DepositFiatMoney depositFiatMoney) {
        LOG.debug("Deposits {} in the wallet {} of the account {}", depositFiatMoney.amount(),
                depositFiatMoney.walletId().toString(), depositFiatMoney.accountId().toString());
        if (!SUPPORTED_FIAT_CURRENCIES.contains(depositFiatMoney.symbol().value())) {
            return HttpResponse.badRequest()
                    .body(new CustomError(HttpStatus.BAD_REQUEST.getCode(),
                            Errors.UNSUPPORTED_FIAT_CURRENTY.getError(),
                            String.format(Errors.UNSUPPORTED_FIAT_CURRENTY.getMessage(), SUPPORTED_FIAT_CURRENCIES)));
        }

        var wallet = accountStore.depositToWallet(depositFiatMoney);

        return HttpResponse.ok().body(wallet);
    }

    @Post(uri = "${micronaut.rest.api.wallet.url.withdraws}",
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON)
    @Operation(summary = "Wallet Withdraw",
            description = "Makes a withdraw to an account wallet"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = Wallet.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data Supplied",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = CustomError.class))}),
            @ApiResponse(responseCode = "404", description = "Wallet not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = CustomError.class))})
    })
    public HttpResponse<RestApiResponse> withdrawMoney(@Body WithdrawFiatMoney withdrawFiatMoney) {
        LOG.debug("Withdraws {} from the wallet {} of the account {}", withdrawFiatMoney.amount(),
                withdrawFiatMoney.walletId().toString(), withdrawFiatMoney.accountId().toString());
        if (!SUPPORTED_FIAT_CURRENCIES.contains(withdrawFiatMoney.symbol().value())) {
            throw new FiatCurrencyNotSupportedException(
                    String.format(Errors.UNSUPPORTED_FIAT_CURRENTY.getMessage(), SUPPORTED_FIAT_CURRENCIES));
        }
        var wallet = accountStore.withdrawFromoWallet(withdrawFiatMoney);

        return HttpResponse.ok().body(wallet);
    }

}
