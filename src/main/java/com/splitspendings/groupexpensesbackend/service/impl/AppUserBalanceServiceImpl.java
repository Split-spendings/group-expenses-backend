package com.splitspendings.groupexpensesbackend.service.impl;

import com.splitspendings.groupexpensesbackend.dto.appuser.balance.AppUserBalanceDto;
import com.splitspendings.groupexpensesbackend.mapper.AppUserBalanceMapper;
import com.splitspendings.groupexpensesbackend.mapper.NetChangeMapper;
import com.splitspendings.groupexpensesbackend.mapper.TransactionMapper;
import com.splitspendings.groupexpensesbackend.model.*;
import com.splitspendings.groupexpensesbackend.model.enums.Currency;
import com.splitspendings.groupexpensesbackend.repository.AppUserBalanceRepository;
import com.splitspendings.groupexpensesbackend.repository.PayoffRepository;
import com.splitspendings.groupexpensesbackend.repository.ShareRepository;
import com.splitspendings.groupexpensesbackend.service.AppUserBalanceService;
import com.splitspendings.groupexpensesbackend.service.GroupMembershipService;
import com.splitspendings.groupexpensesbackend.service.IdentityService;
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
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AppUserBalanceServiceImpl implements AppUserBalanceService {

    private final AppUserBalanceRepository appUserBalanceRepository;
    private final ShareRepository shareRepository;
    private final PayoffRepository payoffRepository;

    private final IdentityService identityService;
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
     *         id of a {@link AppUserBalanceDto} to be returned
     *
     * @return valid {@link AppUserBalanceDto}
     *
     * @throws ResponseStatusException
     *         with status {@link HttpStatus#NOT_FOUND} when there is no {@link AppUserBalanceDto} with given id
     * @throws ResponseStatusException
     *         with status {@link HttpStatus#FORBIDDEN} when current user has no rights to access AppUserBalanceDto with given id
     */
    @Override
    public AppUserBalanceDto appUserBalanceById(Long id) {
        UserBalance userBalanceModel = appUserBalanceModelById(id);
        groupMembershipService.verifyCurrentUserActiveMembershipByGroupId(userBalanceModel.getGroup().getId());
        return appUserBalanceMapper.userBalanceToAppUserBalance(userBalanceModel);
    }

    /**
     * Retrieves {@link UserBalance} from database based on parameters, converts to {@link AppUserBalanceDto}
     * @param groupId
     *         id of a {@link Group} stored {@link UserBalance}
     * @param appUserId
     *          id of second {@link AppUser} stored in {@link UserBalance}
     * @param currency
     *          currency of {@link UserBalance}
     * @return valid {@link AppUserBalanceDto}
     *
     * @throws ResponseStatusException
     *         with status {@link HttpStatus#NOT_FOUND} when there is no {@link UserBalance} with given parameters
     */
    @Override
    public AppUserBalanceDto appUserBalanceByGroupIdAndAppUserIdAndCurrency(Long groupId, UUID appUserId, Currency currency) {
        groupMembershipService.verifyCurrentUserActiveMembershipByGroupId(groupId);

        Optional<UserBalance> userBalanceOptional = appUserBalanceRepository
                .findByAppUserIdsAndGroupIdAndCurrency(appUserId, identityService.currentUserID(), groupId, currency);
        if (userBalanceOptional.isPresent()){
            return appUserBalanceMapper.userBalanceToAppUserBalance(userBalanceOptional.get());
        }

        UserBalance userBalance = appUserBalanceRepository
                .findByAppUserIdsAndGroupIdAndCurrency(identityService.currentUserID(), appUserId, groupId, currency)
                .orElseThrow(() -> LogUtil.logMessageAndReturnResponseStatusException(log, HttpStatus.NOT_FOUND,
                        String.format("UserBalance with appUserIds = {%s, %s}, Group Id = {%d} and Currency = {%s} not found",
                                 identityService.currentUserID(), appUserId, groupId, currency)));
        return appUserBalanceMapper.userBalanceToAppUserBalance(userBalance);
    }

    /**
     * Retrieves {@link UserBalance} from database based on parameters, converts to {@link AppUserBalanceDto}
     * @param groupId
     *         id of a {@link Group} stored {@link UserBalance}
     * @param appUserId
     *          id of second {@link AppUser} stored in {@link UserBalance}
     * @return valid {@link AppUserBalanceDto}
     *
     * @throws ResponseStatusException
     *         with status {@link HttpStatus#NOT_FOUND} when there is no {@link UserBalance} with given parameters
     */
    @Override
    public Iterable<AppUserBalanceDto> appUserBalancesByGroupIdAndAppUserId(Long groupId, UUID appUserId) {
        groupMembershipService.verifyCurrentUserActiveMembershipByGroupId(groupId);
        return appUserBalanceMapper.userBalanceListToAppUserBalanceList(appUserBalanceRepository
                .findAllByAppUserIdsAndGroupId(identityService.currentUserID(), appUserId, groupId));
    }

    /**
     * Recalculates {@link UserBalance}s for every member on a given {@link Group}
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
     * Recalculates {@link UserBalance}s for every member on a given {@link Group} and {@link Currency}
     * @param group
     *          {@link Group} to recalculate {@link UserBalance}
     * @param currency
     *          {@link Currency} to recalculate {@link UserBalance}
     */
    @Override
    public void recalculateAppUserBalanceByGroupAndCurrency(Group group, Currency currency) {
        appUserBalanceRepository.deleteAllByGroupAndCurrency(group, currency);
        Set<Transaction> transactionSet = getAllTransactionsByGroupIdAndCurrency(group.getId(), currency);
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

    /**
     *
     * @param groupId
     *      id of a {@link Group} to retrieve {@link Transaction} from
     * @param currency
     *      currency of {@link UserBalance} to retrieve {@link Transaction} from
     * @return {@link Set<Transaction>} found in {@link Group} with given id
     */
    private Set<Transaction> getAllTransactionsByGroupIdAndCurrency(Long groupId, Currency currency){
        //retrieve Shares and Payoffs by GroupId and Currency
        Set<Share> sharesByGroupId = shareRepository.findAllByGroupIdAndCurrency(groupId, currency);
        Set<Payoff> payoffsByGroupId = payoffRepository.findAllByGroupIdAndCurrency(groupId, currency);

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