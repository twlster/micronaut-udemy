package edu.udemy.micronaut.controller.dto;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;

@Serdeable
public record Quote(Integer id, BigDecimal bid, BigDecimal ask, BigDecimal lastPrice, BigDecimal volume,
                    Symbol symbol) {

}
