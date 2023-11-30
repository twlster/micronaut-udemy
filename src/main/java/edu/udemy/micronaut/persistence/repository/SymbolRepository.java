package edu.udemy.micronaut.persistence.repository;

import edu.udemy.micronaut.persistence.entity.SymbolEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@Repository
public interface SymbolRepository extends CrudRepository<SymbolEntity, Integer> {

    Optional<SymbolEntity> findByValue(String value);
}
