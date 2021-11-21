package com.splitspendings.groupexpensesbackend.service.impl;

import com.splitspendings.groupexpensesbackend.dto.payoff.NewPayoffDto;
import com.splitspendings.groupexpensesbackend.dto.payoff.PayoffDto;
import com.splitspendings.groupexpensesbackend.dto.payoff.UpdatePayoffDto;
import com.splitspendings.groupexpensesbackend.mapper.PayoffMapper;
import com.splitspendings.groupexpensesbackend.model.Payoff;
import com.splitspendings.groupexpensesbackend.repository.PayoffRepository;
import com.splitspendings.groupexpensesbackend.service.GroupMembershipService;
import com.splitspendings.groupexpensesbackend.service.PayoffService;
import com.splitspendings.groupexpensesbackend.util.LogUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Validator;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PayoffServiceImpl implements PayoffService {

    private final Validator validator;

    private final PayoffRepository payoffRepository;

    private final GroupMembershipService groupMembershipService;

    private final PayoffMapper payoffMapper;

    /**
     * @param id
     *         id of a {@link Payoff} to be returned
     *
     * @return valid {@link Payoff}
     *
     * @throws ResponseStatusException
     *         with status {@link HttpStatus#NOT_FOUND} when there is no {@link Payoff} with given id
     */
    @Override
    public Payoff payoffModelById(Long id) {
        return payoffRepository.findById(id)
                .orElseThrow(() -> LogUtil.logMessageAndReturnResponseStatusException(log, HttpStatus.NOT_FOUND,
                        String.format("Payoff with id = {%d} not found", id)));
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
    public PayoffDto payoffById(Long id) {
        Payoff payoffModel = payoffModelById(id);
        groupMembershipService.verifyCurrentUserActiveMembershipByGroupId(payoffModel.getGroup().getId());
        return payoffMapper.payoffToPayoffDto(payoffModel);
    }

    @Override
    public PayoffDto createPayoff(NewPayoffDto newPayoffDto) {
        throw new NotImplementedException();
    }

    @Override
    public PayoffDto updatePayoff(Long id, UpdatePayoffDto updatePayoffDto) {
        throw new NotImplementedException();
    }
}