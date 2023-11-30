package edu.udemy.micronaut.controller.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record Symbol(Integer id, String value){

}
