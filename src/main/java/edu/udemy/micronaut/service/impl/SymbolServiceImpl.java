package edu.udemy.micronaut.service.impl;

import edu.udemy.micronaut.controller.dto.Symbol;
import edu.udemy.micronaut.persistence.repository.SymbolRepository;
import edu.udemy.micronaut.service.mapper.SymbolMapper;
import edu.udemy.micronaut.service.SymbolService;
import io.micronaut.context.annotation.Primary;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;

import java.util.List;

@Singleton
@Primary
@AllArgsConstructor
public class SymbolServiceImpl implements SymbolService {

    private final SymbolRepository symbolRepository;

    private final SymbolMapper symbolMapper;

    @Override
    public List<Symbol> getAllSymbols() {
        return symbolRepository.findAll().stream().map(symbolMapper::mapToSymbol).toList();
    }

    @Override
    public Symbol getSymbolByValue(String value) {
        return symbolMapper.mapToSymbol(symbolRepository.findByValue(value).orElse(null));
    }

}
