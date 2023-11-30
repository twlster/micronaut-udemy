package edu.udemy.micronaut.dataprovider;

import edu.udemy.micronaut.persistence.entity.SymbolEntity;
import edu.udemy.micronaut.persistence.repository.SymbolRepository;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;

import java.util.stream.Stream;

@Singleton
@AllArgsConstructor
public class TestDataProvider {

    private final SymbolRepository symbolRepository;

    @EventListener
    public void init(StartupEvent startupEvent) {
        if (symbolRepository.findAll().isEmpty()) {
            Stream.of("AAPL", "AMZN", "FB", "TSLA")
                    .map(data -> {
                        SymbolEntity symbolEntity = new SymbolEntity();
                        symbolEntity.setValue(data);
                        return symbolEntity;
                    })
                    .forEach(symbolRepository::save);
        }
    }

}
