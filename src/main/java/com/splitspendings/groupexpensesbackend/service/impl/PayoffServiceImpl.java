package com.splitspendings.groupexpensesbackend.service.impl;

import com.splitspendings.groupexpensesbackend.dto.payoff.NewPayoffDto;
import com.splitspendings.groupexpensesbackend.dto.payoff.PayoffDto;
import com.splitspendings.groupexpensesbackend.dto.payoff.UpdatePayoffDto;
import com.splitspendings.groupexpensesbackend.mapper.PayoffMapper;
import com.splitspendings.groupexpensesbackend.model.AppUser;
import com.splitspendings.groupexpensesbackend.model.Group;
import com.splitspendings.groupexpensesbackend.model.Payoff;
import com.splitspendings.groupexpensesbackend.model.enums.Currency;
import com.splitspendings.groupexpensesbackend.repository.PayoffRepository;
import com.splitspendings.groupexpensesbackend.service.AppUserBalanceService;
import com.splitspendings.groupexpensesbackend.service.AppUserService;
import com.splitspendings.groupexpensesbackend.service.GroupMembershipService;
import com.splitspendings.groupexpensesbackend.service.GroupService;
import com.splitspendings.groupexpensesbackend.service.IdentityService;
import com.splitspendings.groupexpensesbackend.service.PayoffService;
import com.splitspendings.groupexpensesbackend.util.LogUtil;
import com.splitspendings.groupexpensesbackend.util.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PayoffServiceImpl implements PayoffService {

    private final Validator validator;

    private final PayoffRepository payoffRepository;

    private final AppUserBalanceService appUserBalanceService;
    private final AppUserService appUserService;
    private final IdentityService identityService;
    private final GroupMembershipService groupMembershipService;
    private final GroupService groupService;

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

    /**
     *
     * @param newPayoffDto
     *          data to create new {@link Payoff}
     *
     * @return created {@link PayoffDto}
     *
     * @throws ConstraintViolationException
     *         when {@link NewPayoffDto} is invalid
     * @throws ResponseStatusException
     *         with status {@link HttpStatus#NOT_FOUND} when there is no {@link Group} with given id
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#FORBIDDEN} if any of {@link AppUser} are not an active member of
     *         {@link Group}
     */
    @Override
    public PayoffDto createPayoff(NewPayoffDto newPayoffDto) {
        ValidatorUtil.validate(validator, newPayoffDto);
        Long groupId = newPayoffDto.getGroupId();
        Group group = groupService.groupModelById(groupId);

        verifyUsersActiveMembership(groupId, newPayoffDto.getPaidForAppUser(), newPayoffDto.getPaidToAppUser());
        AppUser currentAppUser = appUserService.appUserModelById(identityService.currentUserID());
        AppUser paidForAppUser = appUserService.appUserModelById(newPayoffDto.getPaidForAppUser());
        AppUser paidToAppUser = appUserService.appUserModelById(newPayoffDto.getPaidToAppUser());

        Payoff payoff = payoffMapper.newPayoffDtoToPayoff(newPayoffDto, group, currentAppUser, paidForAppUser, paidToAppUser);
        payoffRepository.save(payoff);
        appUserBalanceService.recalculateAppUserBalanceByGroupAndCurrency(group, newPayoffDto.getCurrency());

        return payoffMapper.payoffToPayoffDto(payoff);
    }

    /**
     * @param id
     *      id of a {@link Payoff} to be updated
     * @param updatePayoffDto
     *          data to update {@link Payoff} with
     * @return updated {@link PayoffDto}
     *
     * @throws ConstraintViolationException
     *         when {@link UpdatePayoffDto} is invalid
     * @throws ResponseStatusException
     *         with status {@link HttpStatus#NOT_FOUND} when there is no {@link Payoff} with given id
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#FORBIDDEN} if any of {@link AppUser} are not an active member of
     *         {@link Group}
     */
    @Override
    public PayoffDto updatePayoff(Long id, UpdatePayoffDto updatePayoffDto) {
        ValidatorUtil.validate(validator, updatePayoffDto);
        Payoff payoff = payoffModelById(id);
        Group group = payoff.getGroup();

        verifyUsersActiveMembership(group.getId(), updatePayoffDto.getPaidForAppUser(), updatePayoffDto.getPaidToAppUser());
        AppUser currentAppUser = appUserService.appUserModelById(identityService.currentUserID());
        AppUser paidForAppUser = appUserService.appUserModelById(updatePayoffDto.getPaidForAppUser());
        AppUser paidToAppUser = appUserService.appUserModelById(updatePayoffDto.getPaidToAppUser());

        payoffMapper.copyUpdatePayoffDtoToPayoff(updatePayoffDto, currentAppUser, paidForAppUser, paidToAppUser, payoff);
        payoffRepository.save(payoff);
        appUserBalanceService.recalculateAppUserBalanceByGroup(group);

        return payoffMapper.payoffToPayoffDto(payoff);
    }

    @Override
    public void deletePayoff(Long id) {
        Payoff payoff = payoffModelById(id);

        Group group = payoff.getGroup();
        Currency currency = payoff.getCurrency();

        groupMembershipService.verifyCurrentUserActiveMembershipByGroupId(group.getId());

        payoffRepository.delete(payoff);
        payoffRepository.flush();

        appUserBalanceService.recalculateAppUserBalanceByGroupAndCurrency(group, currency);
    }

    /**
     * verifies whether current and passed {@link AppUser}s are active group members
     * @param groupId
     *          id of a {@link Group}
     * @param paidForAppUser
     *         id of a {@link AppUser} to be checked
     * @param paidToAppUser
     *         id of a {@link AppUser} to be checked
     *
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#FORBIDDEN} if any of {@link AppUser} are not an active member of
     *         {@link Group}
     */
    private void verifyUsersActiveMembership(Long groupId, UUID paidForAppUser, UUID paidToAppUser){
        groupMembershipService.verifyCurrentUserActiveMembershipByGroupId(groupId);
        groupMembershipService.verifyUserActiveMembershipByGroupId(paidForAppUser, groupId);
        groupMembershipService.verifyUserActiveMembershipByGroupId(paidToAppUser, groupId);
    }
}