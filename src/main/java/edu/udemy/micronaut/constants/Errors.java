package edu.udemy.micronaut.constants;

import lombok.Getter;

@Getter
public enum Errors {
    UNSUPPORTED_FIAT_CURRENTY("UNSUPPORTED_FIAT_CURRENTY","Only %s are supported");

    private String error;
    private String message;

    Errors(String error, String message){
        this.error = error;
        this.message = message;
    }
}
