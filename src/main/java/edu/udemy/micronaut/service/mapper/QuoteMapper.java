package edu.udemy.micronaut.service.mapper;

import edu.udemy.micronaut.controller.dto.Quote;
import edu.udemy.micronaut.persistence.entity.QuoteEntity;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;

@Singleton
@AllArgsConstructor
public class QuoteMapper {

    private final SymbolMapper symbolMapper;

    public Quote mapToQuote(QuoteEntity entity) {
        return entity != null ?
                new Quote(entity.getId(),
                        entity.getBid(),
                        entity.getAsk(),
                        entity.getLastPrice(),
                        entity.getVolume(),
                        symbolMapper.mapToSymbol(entity.getSymbol()))
                : null;
    }

    public QuoteEntity mapToQuoteEntity(Quote quote) {
        QuoteEntity quoteEntity = null;
        if (quote != null) {
            quoteEntity = new QuoteEntity();
            quoteEntity.setSymbol(symbolMapper.mapToSymbolEntity(quote.symbol()));
            quoteEntity.setLastPrice(quote.lastPrice());
            quoteEntity.setBid(quote.bid());
            quoteEntity.setAsk(quote.ask());
            quoteEntity.setVolume(quote.volume());
        }
        return quoteEntity;
    }

}
