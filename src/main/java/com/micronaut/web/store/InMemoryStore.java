package com.micronaut.web.store;

import com.github.javafaker.Faker;
import com.micronaut.web.data.Symbol;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Singleton
public class InMemoryStore {

    private final Logger LOG = LoggerFactory.getLogger(InMemoryStore.class);

    private final Map<String, Symbol> symbols = new HashMap<>();
    private final Faker faker = new Faker();

    @PostConstruct
    public void initialize() {
        initializeWith(10);
    }

    public void initializeWith(int noOfEntries) {
        symbols.clear();
        IntStream.range(0, noOfEntries).forEach(i -> {
            generateNewSymbol();
        });
    }

    private void generateNewSymbol() {
        Symbol symbol = new Symbol(faker.stock().nsdqSymbol());
        symbols.put(symbol.getValue(), symbol);
        LOG.debug("Added Symbol {}", symbol);
    }

    public List<Symbol> addSymbol(final Symbol symbol) {
        symbols.put(symbol.getValue(), symbol);
        return new ArrayList<>(symbols.values());
    }

    public List<Symbol> updateSymbol(final Symbol symbol) {
        symbols.put(symbol.getValue(), symbol);
        return new ArrayList<>(symbols.values());
    }

    public List<Symbol> deleteSymbol(final String symbolValue) {
        symbols.remove(symbolValue);
        return new ArrayList<>(symbols.values());
    }

    public Map<String, Symbol> getSymbols() {
        return symbols;
    }
}
