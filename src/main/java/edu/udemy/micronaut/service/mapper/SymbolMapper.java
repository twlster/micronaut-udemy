package edu.udemy.micronaut.service.mapper;

import edu.udemy.micronaut.controller.dto.Symbol;
import edu.udemy.micronaut.persistence.entity.SymbolEntity;
import jakarta.inject.Singleton;

@Singleton
public class SymbolMapper {

    public Symbol mapToSymbol(SymbolEntity entity){
        return entity!=null ? new Symbol(entity.getId(), entity.getValue()) : null;
    }

    public SymbolEntity mapToSymbolEntity(Symbol symbol){
        SymbolEntity symbolEntity = null;
        if(symbol != null){
            symbolEntity = new SymbolEntity();
            symbolEntity.setValue(symbol.value());
        }
        return symbolEntity;
    }

}
