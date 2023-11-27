package edu.udemy.micronaut;

import io.micronaut.http.annotation.*;

@Controller("/micronaut-udemy")
public class MicronautUdemyController {

    @Get(uri="/", produces="text/plain")
    public String index() {
        return "Example Response";
    }
}