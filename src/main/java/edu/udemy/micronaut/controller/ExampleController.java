package edu.udemy.micronaut.controller;

import edu.udemy.micronaut.config.TranslationConfig;
import edu.udemy.micronaut.controller.dto.Symbol;
import edu.udemy.micronaut.controller.dto.error.CustomError;
import edu.udemy.micronaut.service.ExampleService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/micronaut-udemy")
@Tag(name = "Micronaut Example Api")
public class ExampleController {

    private static final Logger LOG = LoggerFactory.getLogger(ExampleController.class);

    //@Inject
    private final ExampleService exampleService;
    private final TranslationConfig translationConfig;

    public ExampleController(ExampleService exampleService, TranslationConfig translationConfig) {
        this.exampleService = exampleService;
        this.translationConfig = translationConfig;
    }

    @Get(uri = "${micronaut.rest.api.example.url}", produces = MediaType.TEXT_PLAIN)
    @Operation(summary = "Example",
            description = "First micronaut example endpoint"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = TranslationConfig.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data Supplied",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = CustomError.class))}),
            @ApiResponse(responseCode = "404", description = "index not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = CustomError.class))})
    })
    public String index() {
        LOG.debug("Called index endpoint");
        return exampleService.exampleResponse();
    }

    @Get(uri = "${micronaut.rest.api.example.translation.url}", produces = MediaType.APPLICATION_JSON)
    @Operation(summary = "Translations",
            description = "Gets examples API translations"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = TranslationConfig.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data Supplied",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = CustomError.class))}),
            @ApiResponse(responseCode = "404", description = "Translation not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = CustomError.class))})
    })
    public TranslationConfig translations() {
        LOG.debug("Called index endpoint");
        return translationConfig;
    }

}