package com.splitspendings.groupexpensesbackend.service.impl;

import com.splitspendings.groupexpensesbackend.mapper.NetChangeMapper;
import com.splitspendings.groupexpensesbackend.mapper.TransactionMapper;
import com.splitspendings.groupexpensesbackend.model.Group;
import com.splitspendings.groupexpensesbackend.model.Payoff;
import com.splitspendings.groupexpensesbackend.model.Share;
import com.splitspendings.groupexpensesbackend.model.UserBalance;
import com.splitspendings.groupexpensesbackend.model.enums.Currency;
import com.splitspendings.groupexpensesbackend.repository.AppUserBalanceRepository;
import com.splitspendings.groupexpensesbackend.repository.PayoffRepository;
import com.splitspendings.groupexpensesbackend.repository.ShareRepository;
import com.splitspendings.groupexpensesbackend.service.AppUserBalanceService;
import com.splitspendings.groupexpensesbackend.service.GroupService;
import com.splitspendings.groupexpensesbackend.service.impl.balance.NetChange;
import com.splitspendings.groupexpensesbackend.service.impl.balance.Transaction;
import com.splitspendings.groupexpensesbackend.util.BalanceCalculatorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AppUserBalanceServiceImpl implements AppUserBalanceService {

    private final AppUserBalanceRepository appUserBalanceRepository;
    private final ShareRepository shareRepository;
    private final PayoffRepository payoffRepository;

    private final TransactionMapper transactionMapper;
    private final NetChangeMapper netChangeMapper;

    private final GroupService groupService;

    @Override
    public void recalculateAppUserBalanceByGroupId(Group group) {
        appUserBalanceRepository.deleteAllByGroup(group);
        Set<Transaction> transactionSet;
        if (group.getSimplifyDebts()){
            transactionSet = getSimplifiedTransactions(group.getId());
        } else {
            transactionSet = getAllTransactionsByGroupId(group.getId());
        }
        Set<UserBalance> userBalanceSet = transactionMapper.transactionSetToUserBalanceSet(transactionSet, group);
        appUserBalanceRepository.saveAll(userBalanceSet);
    }

    @Override
    public void recalculateAppUserBalanceByGroupId(Long groupId) {
        recalculateAppUserBalanceByGroupId(groupService.groupModelById(groupId));
    }

    private Set<Transaction> getSimplifiedTransactions(Long groupId){
        return BalanceCalculatorUtil.calculate(getAllNetChangesByGroupId(groupId));
    }

    private Map<Currency, Set<NetChange>> getAllNetChangesByGroupId(Long groupId){
        return netChangeMapper.transactionListToNetChangeList(getAllTransactionsByGroupId(groupId));
    }

    private Set<Transaction> getAllTransactionsByGroupId(Long groupId){
        //retrieve Shares and Payoffs by GroupId
        Set<Share> sharesByGroupId = shareRepository.findAllByGroupId(groupId);
        Set<Payoff> payoffsByGroupId = payoffRepository.findAllByGroupId(groupId);

        //map Shares and Payoffs to Transactions
        Set<Transaction> sharesAsTransactions = transactionMapper.shareSetToTransactionSet(sharesByGroupId);
        Set<Transaction> payoffsAsTransactions = transactionMapper.payoffSetToTransactionSet(payoffsByGroupId);

        //combine Shares and Payoffs
        Set<Transaction> allTransactionsById = new HashSet<>();
        allTransactionsById.addAll(sharesAsTransactions);
        allTransactionsById.addAll(payoffsAsTransactions);

        return allTransactionsById;
    }
}