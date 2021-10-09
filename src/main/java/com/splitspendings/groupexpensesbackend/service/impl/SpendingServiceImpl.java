package com.splitspendings.groupexpensesbackend.service.impl;

import com.splitspendings.groupexpensesbackend.dto.item.NewItemDto;
import com.splitspendings.groupexpensesbackend.dto.share.NewShareDto;
import com.splitspendings.groupexpensesbackend.dto.spending.NewSpendingDto;
import com.splitspendings.groupexpensesbackend.dto.spending.SpendingCommentsDto;
import com.splitspendings.groupexpensesbackend.dto.spending.SpendingDto;
import com.splitspendings.groupexpensesbackend.mapper.ItemMapper;
import com.splitspendings.groupexpensesbackend.mapper.ShareMapper;
import com.splitspendings.groupexpensesbackend.mapper.SpendingMapper;
import com.splitspendings.groupexpensesbackend.model.*;
import com.splitspendings.groupexpensesbackend.model.enums.Currency;
import com.splitspendings.groupexpensesbackend.repository.ItemRepository;
import com.splitspendings.groupexpensesbackend.repository.ShareRepository;
import com.splitspendings.groupexpensesbackend.repository.SpendingRepository;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SpendingServiceImpl implements SpendingService {

    private final Validator validator;

    private final SpendingRepository spendingRepository;
    private final ItemRepository itemRepository;
    private final ShareRepository shareRepository;

    private final SpendingMapper spendingMapper;
    private final ItemMapper itemMapper;
    private final ShareMapper shareMapper;

    private final GroupMembershipService groupMembershipService;
    private final IdentityService identityService;

    @Override
    public Spending spendingModelById(Long id) {
        return spendingRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Spending not found"));
    }

    @Override
    public SpendingDto spendingById(Long id) {
        Spending spending = spendingModelById(id);
        groupMembershipService.verifyCurrentUserActiveMembershipByGroupId(spending.getAddedByGroupMembership().getGroup().getId());
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
        GroupMembership addedByGroupMembership = groupMembershipService.groupActiveMembershipModelByGroupId(currentAppUserId, newSpendingDto.getGroupID());
        GroupMembership paidByGroupMembership = groupMembershipService.groupMembershipModelById(newSpendingDto.getPaidByGroupMembershipId());

        Group group = addedByGroupMembership.getGroup();

        if(paidByGroupMembership.getGroup() != group) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "paidByGroupMembershipId does not belong to a member of a group");
        }

        Spending spending = spendingMapper.newSpendingDtoToSpending(newSpendingDto);
        spending.setAddedByGroupMembership(addedByGroupMembership);

        Currency currency = spending.getCurrency();

        List<Item> items = new ArrayList<>();
        List<Share> shares = new ArrayList<>();

        BigDecimal totalAmount = new BigDecimal(0);

        for (NewItemDto newItemDto : newSpendingDto.getNewItemDtoList()) {
            Item item = itemMapper.newItemDtoToItem(newItemDto);
            BigDecimal price = new BigDecimal(0);

            for(NewShareDto newShareDto : newItemDto.getNewShareDtoList()) {
                Share share = shareMapper.newShareDtoToShare(newShareDto);

                GroupMembership paidForGroupMembership = groupMembershipService.groupMembershipModelById(newShareDto.getPaidForGroupMembershipId());
                if(paidForGroupMembership.getGroup() != group) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "paidForGroupMembership does not belong to a member of a group");
                }
                share.setPaidByGroupMembership(paidByGroupMembership);
                share.setPaidForGroupMembership(paidForGroupMembership);
                share.setCurrency(currency);
                share.setItem(item);

                shares.add(share);

                price = price.add(newShareDto.getAmount());
            }
            item.setPrice(price);
            item.setSpending(spending);

            items.add(item);

            totalAmount = totalAmount.add(price);
        }

        spending.setTotalAmount(totalAmount);

        Spending createdSpending = spendingRepository.save(spending);
        itemRepository.saveAll(items);
        shareRepository.saveAll(shares);

        return spendingMapper.spendingToSpendingDto(createdSpending);
    }

    @Override
    public SpendingCommentsDto findAllBySpendingId(Long spendingId) {
        Spending spending = spendingRepository.findByIdFetchComments(spendingId, identityService.currentUserID())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("No spending with id = {%d} found", spendingId)));
        return spendingMapper.spendingToSpendingCommentsDto(spending);
    }
}
