package com.splitspendings.groupexpensesbackend.service.impl;

import com.splitspendings.groupexpensesbackend.dto.appuser.balance.AppUserBalanceDto;
import com.splitspendings.groupexpensesbackend.mapper.AppUserBalanceMapper;
import com.splitspendings.groupexpensesbackend.mapper.NetChangeMapper;
import com.splitspendings.groupexpensesbackend.mapper.TransactionMapper;
import com.splitspendings.groupexpensesbackend.model.Group;
import com.splitspendings.groupexpensesbackend.model.Payoff;
import com.splitspendings.groupexpensesbackend.model.Share;
import com.splitspendings.groupexpensesbackend.model.UserBalance;
import com.splitspendings.groupexpensesbackend.repository.AppUserBalanceRepository;
import com.splitspendings.groupexpensesbackend.repository.PayoffRepository;
import com.splitspendings.groupexpensesbackend.repository.ShareRepository;
import com.splitspendings.groupexpensesbackend.service.AppUserBalanceService;
import com.splitspendings.groupexpensesbackend.service.GroupMembershipService;
import com.splitspendings.groupexpensesbackend.service.impl.balance.Transaction;
import com.splitspendings.groupexpensesbackend.util.BalanceCalculatorUtil;
import com.splitspendings.groupexpensesbackend.util.LogUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AppUserBalanceServiceImpl implements AppUserBalanceService {

    private final AppUserBalanceRepository appUserBalanceRepository;
    private final ShareRepository shareRepository;
    private final PayoffRepository payoffRepository;

    private final GroupMembershipService groupMembershipService;

    private final TransactionMapper transactionMapper;
    private final NetChangeMapper netChangeMapper;
    private final AppUserBalanceMapper appUserBalanceMapper;

    /**
     * @param id
     *         id of a {@link UserBalance} to be returned
     *
     * @return valid {@link UserBalance}
     *
     * @throws ResponseStatusException
     *         with status {@link HttpStatus#NOT_FOUND} when there is no {@link UserBalance} with given id
     */
    @Override
    public UserBalance appUserBalanceModelById(Long id) {
        return appUserBalanceRepository.findById(id)
                .orElseThrow(() -> LogUtil.logMessageAndReturnResponseStatusException(log, HttpStatus.NOT_FOUND,
                        String.format("UserBalance with id = {%d} not found", id)));
    }

    /**
     * @param id
     *         id of a {@link Payoff} to be returned
     *
     * @return valid {@link Payoff}
     *
     * @throws ResponseStatusException
     *         with status {@link HttpStatus#NOT_FOUND} when there is no {@link Payoff} with given id
     * @throws ResponseStatusException
     *         with status {@link HttpStatus#FORBIDDEN} when current user has no rights to access Payoff with given id
     */
    @Override
    public AppUserBalanceDto appUserBalanceById(Long id) {
        UserBalance userBalanceModel = appUserBalanceModelById(id);
        groupMembershipService.verifyCurrentUserActiveMembershipByGroupId(userBalanceModel.getGroup().getId());
        return appUserBalanceMapper.userBalanceToAppUserBalance(userBalanceModel);
    }

    /**
     * @param group
     *          {@link Group} to recalculate {@link UserBalance}
     */
    @Override
    public void recalculateAppUserBalanceByGroup(Group group) {
        appUserBalanceRepository.deleteAllByGroup(group);
        Set<Transaction> transactionSet = getAllTransactionsByGroupId(group.getId());
        if (group.getSimplifyDebts()){
            transactionSet = BalanceCalculatorUtil.calculate(netChangeMapper.transactionListToNetChangeList(transactionSet));
        }
        Set<UserBalance> userBalanceSet = transactionMapper.transactionSetToUserBalanceSet(transactionSet, group);
        appUserBalanceRepository.saveAll(userBalanceSet);
    }

    /**
     *
     * @param groupId
     *      id of a {@link Group} to retrieve {@link Transaction} from
     * @return {@link Set<Transaction>} found in {@link Group} with given id
     */
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