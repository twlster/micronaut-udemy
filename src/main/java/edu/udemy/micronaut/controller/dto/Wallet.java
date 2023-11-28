package edu.udemy.micronaut.controller.dto;

import edu.udemy.micronaut.controller.dto.common.RestApiResponse;
import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;
import java.util.UUID;

@Serdeable
public record Wallet(UUID accountId, UUID walletId, Symbol symbol, BigDecimal available, BigDecimal locked)
        implements RestApiResponse {

    public Wallet addAvailable(BigDecimal amountToAdd){
        return new Wallet(
                this.accountId,
                this.walletId,
                this.symbol,
                this.available.add(amountToAdd),
                this.locked
        );
    }
}
