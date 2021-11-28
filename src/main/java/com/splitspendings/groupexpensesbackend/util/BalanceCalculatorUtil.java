package com.splitspendings.groupexpensesbackend.util;

import com.splitspendings.groupexpensesbackend.model.enums.Currency;
import com.splitspendings.groupexpensesbackend.service.impl.balance.NetChange;
import com.splitspendings.groupexpensesbackend.service.impl.balance.Transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BalanceCalculatorUtil {

    private BalanceCalculatorUtil(){

    }

    public static Set<Transaction> calculate(Map<Currency, Set<NetChange>> netChangeSet){
        Set<Transaction> transactionSet = new HashSet<>();
        for (Map.Entry<Currency, Set<NetChange>> entry : netChangeSet.entrySet()){
            transactionSet.addAll(new BalanceCalculator(entry.getKey(), entry.getValue()).calculate());
        }
        return transactionSet;
    }

    private static class BalanceCalculator{
        private final Currency currency;
        private final List<NetChange> netChangeList;
        private final List<NetChange> positiveNetChangeList;
        private final List<NetChange> negativeNetChangeList;
        private final List<Transaction> out = new ArrayList<>();

        public BalanceCalculator(Currency currency, Set<NetChange> netChangeList) {
            this.currency = currency;
            this.netChangeList = new ArrayList<>(netChangeList);
            positiveNetChangeList = getPositiveNetChanges();
            negativeNetChangeList = getNegativeNetChanges();
            // zeroes are ignored
        }

        /**
         * always passes the user who is owed the most by other users. After each iteration positive user is either
         * removed or the dept is decreased
         */
        public List<Transaction> calculate(){
            while (!positiveNetChangeList.isEmpty()){
                sort();
                performIteration(positiveNetChangeList.get(0));
            }
            return out;
        }

        private void performIteration(NetChange positive){
            NetChange previousNegative = null;
            for(NetChange negative : new ArrayList<>(negativeNetChangeList)){
                BigDecimal absNegativeAmount = negative.getAmount().abs();

                //checks if the first negative has the debt smaller than the first positive
                if ((previousNegative == null) && absNegativeAmount.compareTo(positive.getAmount()) < 0){
                    performTransaction(negative, positive, absNegativeAmount);
                    return;
                }
                //checks if current negative debt is the last one
                if (negativeNetChangeList.get(negativeNetChangeList.size() - 1).equals(negative)){
                    performTransaction(negative, positive, positive.getAmount());
                    return;
                }
                //finds the closest negative dept to current positive
                if (absNegativeAmount.compareTo(positive.getAmount()) > 0){
                    previousNegative = negative;
                    continue;
                }

                if (absNegativeAmount.compareTo(positive.getAmount()) < 0){
                    assert previousNegative != null;
                    performTransaction(previousNegative, positive, positive.getAmount());
                    return;
                }
            }
        }

        private void performTransaction(NetChange negative, NetChange positive, BigDecimal amount){
            negative.addAmount(amount);
            positive.subtractAmount(amount);

            Transaction transaction = new Transaction(negative.getAppUser(), positive.getAppUser(), amount, currency);
            out.add(transaction);

            if (negative.getAmount().compareTo(BigDecimal.ZERO) == 0){
                negativeNetChangeList.remove(negative);
            }

            if (positive.getAmount().compareTo(BigDecimal.ZERO) == 0){
                positiveNetChangeList.remove(positive);
            }
        }

        private void sort(){
            positiveNetChangeList.sort(NetChange::compareTo);
            negativeNetChangeList.sort(NetChange::compareTo);
        }

        private List<NetChange> getPositiveNetChanges(){
            return netChangeList.stream().filter(n -> n.getAmount().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
        }

        private List<NetChange> getNegativeNetChanges(){
            return netChangeList.stream().filter(n -> n.getAmount().compareTo(BigDecimal.ZERO) < 0).collect(Collectors.toList());
        }
    }
}
