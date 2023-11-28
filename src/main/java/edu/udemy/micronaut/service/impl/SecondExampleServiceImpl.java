package edu.udemy.micronaut.service.impl;

import edu.udemy.micronaut.service.ExampleService;
import jakarta.inject.Singleton;

@Singleton
public class SecondExampleServiceImpl implements ExampleService {

    public String exampleResponse(){
        return "Second Example Response";
    }

}
