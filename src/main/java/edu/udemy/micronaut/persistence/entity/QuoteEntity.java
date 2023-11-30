package edu.udemy.micronaut.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity(name = "quote")
@Table(name = "quotes", schema = "mn")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private BigDecimal bid;
    private BigDecimal ask;
    @Column(name = "last_price")
    private BigDecimal lastPrice;
    private BigDecimal volume;

    @ManyToOne(targetEntity = SymbolEntity.class)
    @JoinColumn(name="symbol", referencedColumnName = "id")
    private SymbolEntity symbol;

}
