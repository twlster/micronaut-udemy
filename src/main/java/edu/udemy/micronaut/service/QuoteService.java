package edu.udemy.micronaut.service;

import edu.udemy.micronaut.controller.dto.Quote;

import java.util.List;

public interface QuoteService {

    List<Quote> getAllQuotes();

    List<Quote> getAllQuotesBySymbol(String value);
}
