package edu.udemy.micronaut.controller.dto;

import io.micronaut.serde.annotation.Serdeable;

import java.util.ArrayList;
import java.util.List;

@Serdeable
public record WatchList(List<Symbol> symbols) {

    public WatchList() {
        this(new ArrayList<>());
    }
}
