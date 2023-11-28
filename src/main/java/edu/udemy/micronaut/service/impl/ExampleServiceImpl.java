package edu.udemy.micronaut.service.impl;

import edu.udemy.micronaut.service.ExampleService;
import io.micronaut.context.annotation.Primary;
import jakarta.inject.Singleton;

@Singleton
@Primary
public class ExampleServiceImpl implements ExampleService {

    public String exampleResponse(){
        return "Example Response";
    }

}
