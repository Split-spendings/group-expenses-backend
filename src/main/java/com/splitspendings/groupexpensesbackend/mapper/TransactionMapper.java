package com.splitspendings.groupexpensesbackend.mapper;

import com.splitspendings.groupexpensesbackend.model.Group;
import com.splitspendings.groupexpensesbackend.model.Payoff;
import com.splitspendings.groupexpensesbackend.model.Share;
import com.splitspendings.groupexpensesbackend.model.UserBalance;
import com.splitspendings.groupexpensesbackend.service.impl.balance.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mapping(target = "from", source = "share.paidByGroupMembership.appUser")
    @Mapping(target = "to", source = "share.paidForGroupMembership.appUser")
    Transaction shareToTransaction(Share share);

    Set<Transaction> shareSetToTransactionSet(Set<Share> shareList);

    @Mapping(target = "from", source = "payoff.paidForAppUser")
    @Mapping(target = "to", source = "payoff.paidToAppUser")
    Transaction payoffToTransaction(Payoff payoff);

    Set<Transaction> payoffSetToTransactionSet(Set<Payoff> payoffList);

    @Mapping(target = "firstAppUser", source = "transaction.from")
    @Mapping(target = "secondAppUser", source = "transaction.to")
    @Mapping(target = "balance", source = "transaction.amount")
    @Mapping(target = "group", source = "group")
    @Mapping(target = "id", ignore = true)
    UserBalance transactionToUserBalance(Transaction transaction, Group group);

    default Set<UserBalance> transactionSetToUserBalanceSet(Set<Transaction> transactionSet, Group group){
        return transactionSet.stream().map(t -> transactionToUserBalance(t, group)).collect(Collectors.toSet());
    }
}
