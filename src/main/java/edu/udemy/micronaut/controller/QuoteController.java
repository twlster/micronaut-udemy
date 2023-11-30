package edu.udemy.micronaut.controller;

import edu.udemy.micronaut.controller.dto.Quote;
import edu.udemy.micronaut.controller.dto.Symbol;
import edu.udemy.micronaut.controller.dto.error.CustomError;
import edu.udemy.micronaut.service.QuoteService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

@Controller("/quotes")
@Tag(name = "Quote Api")
@Secured(SecurityRule.IS_AUTHENTICATED)
@AllArgsConstructor
public class QuoteController {

    private static final Logger LOG = LoggerFactory.getLogger(QuoteController.class);

    private final QuoteService quoteService;

    @Get(uri = "${micronaut.rest.api.quote.url}", produces = MediaType.APPLICATION_JSON)
    @Operation(summary = "Quotes",
            description = "Returns the list of quotes of the application"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            array = @ArraySchema(items = @Schema(implementation = Symbol.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid data Supplied",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = CustomError.class))}),
            @ApiResponse(responseCode = "404", description = "Quote not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = CustomError.class))})
    })
    public Collection<Quote> quotes() {
        LOG.debug("Recovering all quotes");
        return quoteService.getAllQuotes();
    }

    @Get(uri = "${micronaut.rest.api.quote.url.symbol}", produces = MediaType.APPLICATION_JSON)
    @Operation(summary = "Quotes by Symbol",
            description = "Returns the quotes related to a symbol of the application"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = Symbol.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data Supplied",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = CustomError.class))}),
            @ApiResponse(responseCode = "404", description = "Quote not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = CustomError.class))})
    })
    public Collection<Quote> quotesBySymbol(@PathVariable("symbol") String symbol) {
        LOG.debug("Recovering the quotes based on the symbol {}", symbol);
        return quoteService.getAllQuotesBySymbol(symbol);
    }

}
