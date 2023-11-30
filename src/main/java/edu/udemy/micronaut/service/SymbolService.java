package edu.udemy.micronaut.service;

import edu.udemy.micronaut.controller.dto.Symbol;

import java.util.List;

public interface SymbolService {

    List<Symbol> getAllSymbols();

    Symbol getSymbolByValue(String value);
}
