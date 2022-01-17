package com.splitspendings.groupexpensesbackend.service.impl;

import com.splitspendings.groupexpensesbackend.dto.item.NewItemDto;
import com.splitspendings.groupexpensesbackend.dto.share.NewShareDto;
import com.splitspendings.groupexpensesbackend.dto.spending.NewSpendingDto;
import com.splitspendings.groupexpensesbackend.dto.spending.SpendingCommentsDto;
import com.splitspendings.groupexpensesbackend.dto.spending.SpendingDto;
import com.splitspendings.groupexpensesbackend.dto.spending.SpendingShortDto;
import com.splitspendings.groupexpensesbackend.mapper.ItemMapper;
import com.splitspendings.groupexpensesbackend.mapper.ShareMapper;
import com.splitspendings.groupexpensesbackend.mapper.SpendingMapper;
import com.splitspendings.groupexpensesbackend.model.AppUser;
import com.splitspendings.groupexpensesbackend.model.Group;
import com.splitspendings.groupexpensesbackend.model.GroupMembership;
import com.splitspendings.groupexpensesbackend.model.Item;
import com.splitspendings.groupexpensesbackend.model.Share;
import com.splitspendings.groupexpensesbackend.model.Spending;
import com.splitspendings.groupexpensesbackend.model.SpendingComment;
import com.splitspendings.groupexpensesbackend.model.enums.Currency;
import com.splitspendings.groupexpensesbackend.repository.ItemRepository;
import com.splitspendings.groupexpensesbackend.repository.ShareRepository;
import com.splitspendings.groupexpensesbackend.repository.SpendingRepository;
import com.splitspendings.groupexpensesbackend.service.AppUserBalanceService;
import com.splitspendings.groupexpensesbackend.service.GroupMembershipService;
import com.splitspendings.groupexpensesbackend.service.IdentityService;
import com.splitspendings.groupexpensesbackend.service.SpendingService;
import com.splitspendings.groupexpensesbackend.util.LogUtil;
import com.splitspendings.groupexpensesbackend.util.ValidatorUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
    private final AppUserBalanceService appUserBalanceService;

    /**
     * @param id
     *         id of a {@link Spending} to be found
     *
     * @return {@link Spending} with given id
     *
     * @throws ResponseStatusException
     *         with status {@link HttpStatus#NOT_FOUND} when there is no {@link Spending} with given id
     */
    @Override
    public Spending spendingModelById(Long id) {
        return spendingRepository.findById(id)
                .orElseThrow(() -> LogUtil.logMessageAndReturnResponseStatusException(log, HttpStatus.NOT_FOUND,
                        String.format("No spending with id = {%d} found", id)));
    }

    /**
     * @param id
     *         id of a {@link Spending} to be found
     *
     * @return {@link SpendingDto} with given id
     *
     * @throws ResponseStatusException
     *         with status {@link HttpStatus#NOT_FOUND} when there is no {@link Spending} with given id
     * @throws ResponseStatusException
     *         with status {@link HttpStatus#FORBIDDEN} when current {@link AppUser} has no rights to access {@link
     *         Spending} with given id
     */
    @Override
    public SpendingDto spendingById(Long id) {
        Spending spending = spendingModelById(id);
        verifyCurrentUserActiveMembershipBySpending(spending);
        var spendingDto = spendingMapper.spendingToSpendingDto(spending);
        log.info("spendingById {}", spendingDto.toString());
        return spendingDto;
    }

    /**
     * @param newSpendingDto
     *         data to create new {@link Spending}
     *
     * @return {@link SpendingDto} with given id
     *
     * @throws ConstraintViolationException
     *         when {@link NewSpendingDto} is invalid
     * @throws ResponseStatusException
     *         with status {@link HttpStatus#NOT_FOUND} when there is no {@link Spending} with given id
     * @throws ResponseStatusException
     *         with status {@link HttpStatus#NOT_FOUND} when there is no active {@link GroupMembership} with given
     *         {@link AppUser} and {@link Group}
     * @throws ResponseStatusException
     *         with status {@link HttpStatus#NOT_FOUND} when there is no active {@link GroupMembership} with given id
     * @throws ResponseStatusException
     *         with status {@link HttpStatus#FORBIDDEN} when current {@link AppUser} has no rights to access {@link
     *         Spending} with given id
     * @throws ResponseStatusException
     *         with status {@link HttpStatus#BAD_REQUEST} when paid by {@link AppUser} is not an active member of {@link
     *         Group} with given id
     * @throws ResponseStatusException
     *         with status {@link HttpStatus#BAD_REQUEST} when paid for {@link AppUser} is not an active member of
     *         {@link Group} with given id
     */
    @Override
    public SpendingShortDto createSpending(NewSpendingDto newSpendingDto) {
        ValidatorUtil.validate(validator, newSpendingDto);

        UUID currentAppUserId = identityService.currentUserID();
        GroupMembership addedByGroupMembership = groupMembershipService.groupActiveMembershipModelByGroupId(currentAppUserId, newSpendingDto.getGroupID());
        GroupMembership paidByGroupMembership = groupMembershipService.groupMembershipModelById(newSpendingDto.getPaidByGroupMembershipId());

        Group group = addedByGroupMembership.getGroup();

        if (paidByGroupMembership.getGroup() != group) {
            throw LogUtil.logMessageAndReturnResponseStatusException(log, HttpStatus.BAD_REQUEST,
                    "paidByGroupMembershipId does not belong to a member of a group");
        }

        Spending spending = spendingMapper.newSpendingDtoToSpending(newSpendingDto);
        spending.setAddedByGroupMembership(addedByGroupMembership);
        spending.setPaidByGroupMembership(paidByGroupMembership);

        if (spending.getCurrency() == null) {
            spending.setCurrency(group.getDefaultCurrency());
        }

        Currency currency = spending.getCurrency();

        List<Item> items = new ArrayList<>();
        List<Share> shares = new ArrayList<>();

        BigDecimal totalAmount = new BigDecimal(0);

        for (NewItemDto newItemDto : newSpendingDto.getNewItemDtoList()) {
            Item item = itemMapper.newItemDtoToItem(newItemDto);
            BigDecimal price = new BigDecimal(0);

            for (NewShareDto newShareDto : newItemDto.getNewShareDtoList()) {
                Share share = shareMapper.newShareDtoToShare(newShareDto);

                GroupMembership paidForGroupMembership = groupMembershipService.groupMembershipModelById(newShareDto.getPaidForGroupMembershipId());
                if (paidForGroupMembership.getGroup() != group) {
                    throw LogUtil.logMessageAndReturnResponseStatusException(log, HttpStatus.BAD_REQUEST,
                            "paidForGroupMembership does not belong to a member of a group");
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
        appUserBalanceService.recalculateAppUserBalanceByGroupAndCurrency(group, currency);

        return spendingMapper.spendingToSpendingShortDto(createdSpending);
    }

    /**
     * @param spendingId
     *         id of a {@link Spending} to find all {@link SpendingComment}
     *
     * @return all {@link SpendingComment} of a given {@link Spending}
     *
     * @throws ResponseStatusException
     *         with status {@link HttpStatus#NOT_FOUND} when there is no {@link Spending} with given id
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#FORBIDDEN} if current {@link AppUser} is not an active member of
     *         {@link Group}
     */
    @Override
    public SpendingCommentsDto getSpendingComments(Long spendingId) {
        Spending spending = spendingModelById(spendingId);
        verifyCurrentUserActiveMembershipBySpending(spending);
        return spendingMapper.spendingToSpendingCommentsDto(spending);
    }

    /**
     * Delegates job of checking whether current {@link AppUser} is an active member of a {@link Group}
     */
    private void verifyCurrentUserActiveMembershipBySpending(Spending spending) {
        Long groupId = spending.getAddedByGroupMembership().getGroup().getId();
        groupMembershipService.verifyCurrentUserActiveMembershipByGroupId(groupId);
    }
}
