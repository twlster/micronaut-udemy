package edu.udemy.micronaut.controller;

import edu.udemy.micronaut.controller.dto.Symbol;
import edu.udemy.micronaut.controller.dto.error.CustomError;
import edu.udemy.micronaut.controller.dto.inmuable.InMemoryStore;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Optional;

@Controller("/symbols")
@Tag(name = "Symbols Api")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class SymbolController {

    private static final Logger LOG = LoggerFactory.getLogger(SymbolController.class);

    @Inject
    private InMemoryStore inMemoryStore;

    @Get(uri = "${micronaut.rest.api.symbol.url}", produces = MediaType.APPLICATION_JSON)
    @Operation(summary = "Symbols",
            description = "Returns the list of symbols of the application"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            array = @ArraySchema(items = @Schema(implementation = Symbol.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid data Supplied",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = CustomError.class))}),
            @ApiResponse(responseCode = "404", description = "Symbol not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = CustomError.class))})
    })
    public Collection<Symbol> symbols() {
        LOG.debug("Recovering all symbols");
        return inMemoryStore.getSymbols().values();
    }

    @Get(uri = "${micronaut.rest.api.symbol.url.symbol}", produces = MediaType.APPLICATION_JSON)
    @Operation(summary = "Symbol Detail",
            description = "Returns a symbol of the application"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = Symbol.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data Supplied",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = CustomError.class))}),
            @ApiResponse(responseCode = "404", description = "Symbol not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = CustomError.class))})
    })
    public Symbol symbol(@PathVariable("symbol") String symbol) {
        LOG.debug("Recovering the symbol {}", symbol);
        return inMemoryStore.getSymbols().get(symbol);
    }

    @Get(uri = "${micronaut.rest.api.symbol.url.filter}", produces = MediaType.APPLICATION_JSON)
    @Operation(summary = "Filtered Symbols",
            description = "Returns a list of symbol of the application filtered by max and offset values"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = Symbol[].class))),
            @ApiResponse(responseCode = "400", description = "Invalid data Supplied",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = CustomError.class))}),
            @ApiResponse(responseCode = "404", description = "Symbol not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = CustomError.class))})
    })
    public Collection<Symbol> filter(@QueryValue("max") Optional<Integer> max, @QueryValue("offset") Optional<Integer> offset) {
        LOG.debug("Recovering all the symbol filtered by max {} and offset {}", max, offset);
        return inMemoryStore.getSymbols().values()
                .stream()
                .skip(offset.orElse(0))
                .limit(max.orElse(10))
                .toList();
    }

}
