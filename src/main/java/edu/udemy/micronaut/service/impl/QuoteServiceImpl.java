package edu.udemy.micronaut.service.impl;

import edu.udemy.micronaut.controller.dto.Quote;
import edu.udemy.micronaut.persistence.repository.QuoteRepository;
import edu.udemy.micronaut.service.QuoteService;
import edu.udemy.micronaut.service.mapper.QuoteMapper;
import io.micronaut.context.annotation.Primary;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;

import java.util.List;

@Singleton
@Primary
@AllArgsConstructor
public class QuoteServiceImpl implements QuoteService {

    private final QuoteRepository quoteRepository;
    private final QuoteMapper quoteMapper;

    @Override
    public List<Quote> getAllQuotes() {
        return quoteRepository.findAllOrderBySymbolValueDesc().stream().map(quoteMapper::mapToQuote).toList();
    }

    @Override
    public List<Quote> getAllQuotesBySymbol(String value) {
        return quoteRepository.findBySymbolValue(value).stream().map(quoteMapper::mapToQuote).toList();
    }

}
