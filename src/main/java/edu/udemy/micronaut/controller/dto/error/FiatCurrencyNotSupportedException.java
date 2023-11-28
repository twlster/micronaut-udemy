package edu.udemy.micronaut.controller.dto.error;

public class FiatCurrencyNotSupportedException extends RuntimeException{

    public FiatCurrencyNotSupportedException(String message){
        super(message);
    }
}
