package edu.udemy.micronaut.persistence.dataprovider;

import edu.udemy.micronaut.persistence.entity.QuoteEntity;
import edu.udemy.micronaut.persistence.entity.SymbolEntity;
import edu.udemy.micronaut.persistence.repository.QuoteRepository;
import edu.udemy.micronaut.persistence.repository.SymbolRepository;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

@Singleton
@AllArgsConstructor
@Requires(notEnv = Environment.TEST)
public class TestDataProvider {

    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    private final SymbolRepository symbolRepository;
    private final QuoteRepository quoteRepository;

    @EventListener
    public void init(StartupEvent startupEvent) {
        if (symbolRepository.findAll().isEmpty()) {
            Stream.of("AAPL", "TSLA", "AMZN", "FB")
                    .map(data -> {
                        SymbolEntity symbolEntity = new SymbolEntity();
                        symbolEntity.setValue(data);
                        return symbolEntity;
                    })
                    .forEach(symbolRepository::save);
        }
        if (quoteRepository.findAll().isEmpty()) {
            symbolRepository.findAll().forEach(symbol -> {
                var quoteEntity = new QuoteEntity();
                quoteEntity.setAsk(randomBigDecimal());
                quoteEntity.setBid(randomBigDecimal());
                quoteEntity.setVolume(randomBigDecimal());
                quoteEntity.setLastPrice(randomBigDecimal());
                quoteEntity.setSymbol(symbol);
                quoteRepository.save(quoteEntity);
            });
        }
    }

    private BigDecimal randomBigDecimal() {
        return BigDecimal.valueOf(RANDOM.nextDouble(1, 100));
    }

}
