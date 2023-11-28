package edu.udemy.micronaut.controller.dto.inmuable;

import edu.udemy.micronaut.constants.Constants;
import edu.udemy.micronaut.controller.dto.DepositFiatMoney;
import edu.udemy.micronaut.controller.dto.Symbol;
import edu.udemy.micronaut.controller.dto.Wallet;
import edu.udemy.micronaut.controller.dto.WatchList;
import edu.udemy.micronaut.controller.dto.WithdrawFiatMoney;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class InMemoryAccountStore {

    private static final Logger LOG = LoggerFactory.getLogger(InMemoryStore.class);

    private final Map<UUID, WatchList> watchListMap = new HashMap<>();
    private final Map<UUID, Map<UUID, Wallet>> walletPerAccount = new HashMap<>();

    public WatchList getWatchList(final UUID uuid) {
        return watchListMap.getOrDefault(uuid, new WatchList());
    }

    public WatchList updateWatchList(final UUID uuid, final WatchList watchList) {
        watchListMap.put(uuid, watchList);
        return getWatchList(uuid);
    }

    public void deleteWatchList(final UUID uuid) {
        watchListMap.remove(uuid);
    }

    public void deleteWallet(final UUID uuid) {
        walletPerAccount.remove(uuid);
    }

    public Collection<Wallet> getWallets(final UUID accounid) {
        return Optional.ofNullable(walletPerAccount.get(accounid)).orElse(new HashMap<>()).values();
    }

    public Wallet depositToWallet(DepositFiatMoney depositFiatMoney) {
        return addAvailableInWallet(depositFiatMoney.accountId(), depositFiatMoney.walletId(), depositFiatMoney.symbol(), depositFiatMoney.amount());
    }

    public Wallet withdrawFromoWallet(WithdrawFiatMoney withdrawFiatMoney) {
        return addAvailableInWallet(withdrawFiatMoney.accountId(), withdrawFiatMoney.walletId(), withdrawFiatMoney.symbol(), withdrawFiatMoney.amount());
    }

    private Wallet addAvailableInWallet(UUID accountId, UUID walletId, Symbol symbol, BigDecimal changeAmount) {
        final var wallets =
                Optional.ofNullable(walletPerAccount.get(accountId)).orElse(new HashMap<>());

        final var oldWallet = Optional.ofNullable(wallets.get(walletId)).orElse(
                new Wallet(Constants.ACCOUNT_ID, walletId, symbol,
                        BigDecimal.ZERO, BigDecimal.ZERO));

        final var newWallet = oldWallet.addAvailable(changeAmount);

        wallets.put(newWallet.walletId(), newWallet);

        walletPerAccount.put(newWallet.accountId(), wallets);

        return newWallet;
    }

}
