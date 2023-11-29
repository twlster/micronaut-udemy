package edu.udemy.micronaut.controller;

import edu.udemy.micronaut.constants.Constants;
import edu.udemy.micronaut.controller.dto.WatchList;
import edu.udemy.micronaut.controller.dto.error.CustomError;
import edu.udemy.micronaut.controller.dto.inmuable.InMemoryAccountStore;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.Status;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/accounts/watchlist")
@Tag(name = "Accounts Watchlist Api")
@Secured(SecurityRule.IS_AUTHENTICATED)
public record WatchListController(InMemoryAccountStore accountStore) {

    private static final Logger LOG = LoggerFactory.getLogger(WatchListController.class);

    @Get(uri = "${micronaut.rest.api.account.url}",
            produces = MediaType.APPLICATION_JSON)
    @Operation(summary = "Account WatchList",
            description = "Returns the watchlist of an account"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = WatchList.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data Supplied",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = CustomError.class))}),
            @ApiResponse(responseCode = "404", description = "Watchlist not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = CustomError.class))})
    })
    public WatchList getWatchList() {
        LOG.debug("Recovering the watchlist of an account");
        return accountStore.getWatchList(Constants.ACCOUNT_ID);
    }

    @Put(uri = "${micronaut.rest.api.account.url}",
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON)
    @Operation(summary = "Update Account WatchList",
            description = "Updates the watchlist of an account"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = WatchList.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data Supplied",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = CustomError.class))}),
            @ApiResponse(responseCode = "404", description = "Watchlist not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = CustomError.class))})
    })
    public WatchList updateWatchList(@Body WatchList watchList) {
        LOG.debug("Updates the watchlist of an account");
        return accountStore.updateWatchList(Constants.ACCOUNT_ID, watchList);
    }

    @Delete(uri = "${micronaut.rest.api.account.url}")
    @Status(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes Account WatchList",
            description = "Deletes the watchlist of an account"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Data deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid data Supplied",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = CustomError.class))}),
            @ApiResponse(responseCode = "404", description = "Watchlist not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = CustomError.class))})
    })
    public void deleteWatchList() {
        LOG.debug("Deletes the watchlist of an account");
        accountStore.deleteWatchList(Constants.ACCOUNT_ID);
    }

}
