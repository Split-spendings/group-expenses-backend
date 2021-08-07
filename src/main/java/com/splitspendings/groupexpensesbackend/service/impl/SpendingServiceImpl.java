package com.splitspendings.groupexpensesbackend.service.impl;

import com.splitspendings.groupexpensesbackend.dto.spending.SpendingDto;
import com.splitspendings.groupexpensesbackend.mapper.SpendingMapper;
import com.splitspendings.groupexpensesbackend.model.Spending;
import com.splitspendings.groupexpensesbackend.repository.SpendingRepository;
import com.splitspendings.groupexpensesbackend.service.GroupMembershipService;
import com.splitspendings.groupexpensesbackend.service.SpendingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SpendingServiceImpl implements SpendingService {

    private final SpendingRepository spendingRepository;

    private final SpendingMapper spendingMapper;

    private final GroupMembershipService groupMembershipService;

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
}
