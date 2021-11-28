package com.splitspendings.groupexpensesbackend.mapper;

import com.splitspendings.groupexpensesbackend.model.AppUser;
import com.splitspendings.groupexpensesbackend.model.enums.Currency;
import com.splitspendings.groupexpensesbackend.service.impl.balance.NetChange;
import com.splitspendings.groupexpensesbackend.service.impl.balance.Transaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public class NetChangeMapper {
    public Map<Currency, Set<NetChange>> transactionListToNetChangeList(Set<Transaction> transactionSet){
        Map<Currency, Set<NetChange>> out = new EnumMap<>(Currency.class);
        for (Transaction transaction : transactionSet){
            addAmount(out, transaction.getFrom(), transaction.getCurrency(), transaction.getAmount());
            subtractAmount(out, transaction.getTo(), transaction.getCurrency(), transaction.getAmount());
        }
        return out;
    }

    private static void addAmount(Map<Currency, Set<NetChange>> netChangeSet, AppUser appUser, Currency currency, BigDecimal amountToAdd){
        findOrAdd(netChangeSet, appUser, currency).addAmount(amountToAdd);
    }

    private static void subtractAmount(Map<Currency, Set<NetChange>> netChangeSet, AppUser appUser, Currency currency, BigDecimal amountToSubtract){
        findOrAdd(netChangeSet, appUser, currency).subtractAmount(amountToSubtract);
    }

    private static NetChange findOrAdd(Map<Currency, Set<NetChange>> map, AppUser appUser, Currency currency){
        Set<NetChange> netChangeSet = map.getOrDefault(currency, new HashSet<>());
        Optional<NetChange> actual = netChangeSet.stream().filter(item -> item.getAppUser().equals(appUser)).findFirst();

        if (actual.isPresent()){
            return actual.get();
        }

        map.putIfAbsent(currency, netChangeSet);
        NetChange newNetChange = new NetChange(appUser);
        netChangeSet.add(newNetChange);
        return newNetChange;
    }
}