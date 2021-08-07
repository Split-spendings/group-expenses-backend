package com.splitspendings.groupexpensesbackend.service.impl;

import com.splitspendings.groupexpensesbackend.dto.spending.NewSpendingDto;
import com.splitspendings.groupexpensesbackend.dto.spending.SpendingDto;
import com.splitspendings.groupexpensesbackend.mapper.SpendingMapper;
import com.splitspendings.groupexpensesbackend.model.GroupMembership;
import com.splitspendings.groupexpensesbackend.model.Spending;
import com.splitspendings.groupexpensesbackend.repository.SpendingRepository;
import com.splitspendings.groupexpensesbackend.service.AppUserService;
import com.splitspendings.groupexpensesbackend.service.GroupMembershipService;
import com.splitspendings.groupexpensesbackend.service.IdentityService;
import com.splitspendings.groupexpensesbackend.service.SpendingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SpendingServiceImpl implements SpendingService {

    private final Validator validator;

    private final SpendingRepository spendingRepository;

    private final SpendingMapper spendingMapper;

    private final GroupMembershipService groupMembershipService;
    private final IdentityService identityService;
    private final AppUserService appUserService;

    @Override
    public Spending spendingModelById(Long id) {
        Optional<Spending> spending = spendingRepository.findById(id);
        if (spending.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Spending not found");
        }
        return spending.get();
    }

    @Override
    public SpendingDto spendingById(Long id) {
        Spending spending = spendingModelById(id);
        groupMembershipService.verifyCurrentUserActiveMembership(spending.getAddedByGroupMembership().getGroup().getId());
        return spendingMapper.spendingToSpendingDto(spending);
    }

    @Override
    public SpendingDto createSpending(NewSpendingDto newSpendingDto) {
        newSpendingDto.trim();

        Set<ConstraintViolation<NewSpendingDto>> violations = validator.validate(newSpendingDto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        UUID currentAppUserId = identityService.currentUserID();
        GroupMembership groupMembership = groupMembershipService.groupActiveMembershipModel(currentAppUserId, newSpendingDto.getGroupID());

        log.debug("validation ok");

        return null;
    }
}
