package edu.udemy.micronaut.controller.dto;

import io.micronaut.serde.annotation.Serdeable;

import java.io.Serializable;

@Serdeable
public record Symbol(String value){

}
