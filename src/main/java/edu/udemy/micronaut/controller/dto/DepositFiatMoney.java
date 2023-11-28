package edu.udemy.micronaut.controller.dto;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;
import java.util.UUID;

@Serdeable
public record DepositFiatMoney(UUID accountId, UUID walletId, Symbol symbol, BigDecimal amount) {
}
