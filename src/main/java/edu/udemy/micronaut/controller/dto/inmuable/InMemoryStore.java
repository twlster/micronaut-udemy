package edu.udemy.micronaut.controller.dto.inmuable;

import edu.udemy.micronaut.controller.dto.Symbol;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;
import lombok.Getter;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@Singleton
@Getter
public class InMemoryStore {
    private static final Logger LOG = LoggerFactory.getLogger(InMemoryStore.class);
    private final Map<String, Symbol> symbols = new HashMap<>();

    private final Faker faker;

    public InMemoryStore(){
        this.faker = new Faker();
    }

    @PostConstruct
    public void init(){
        init(10);
    }

    public void init(int limit){
        symbols.clear();
        IntStream.range(0, limit).forEach(i -> {
            addNewSymbol();
        });
    }

    private void addNewSymbol(){
        var symbol = new Symbol(faker.stock().hashCode(), faker.stock().nsdqSymbol());
        symbols.put(symbol.value(), symbol);
        LOG.debug("Added Symbol: {}", symbol);
    }
}
