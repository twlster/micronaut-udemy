package edu.udemy.micronaut.persistence.repository;

import edu.udemy.micronaut.persistence.entity.QuoteEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface QuoteRepository extends CrudRepository<QuoteEntity, Integer> {

    List<QuoteEntity> findAllOrderBySymbolValueDesc();
    List<QuoteEntity> findBySymbolValue(String value);

}
