package com.splitspendings.groupexpensesbackend.repository;

import com.splitspendings.groupexpensesbackend.model.Group;
import com.splitspendings.groupexpensesbackend.model.UserBalance;
import com.splitspendings.groupexpensesbackend.model.enums.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppUserBalanceRepository extends JpaRepository<UserBalance, Long> {

    void deleteAllByGroup(Group group);

    void deleteAllByGroupAndCurrency(Group group, Currency currency);

    @Query(" SELECT u_b " +
            "FROM UserBalance  u_b " +
            "WHERE u_b.firstAppUser.id = ?1 OR u_b.secondAppUser.id = ?1 ")
    List<UserBalance> findAllByAppUserId(UUID firstAppUserId);

    @Query(" SELECT u_b " +
            "FROM UserBalance  u_b " +
            "WHERE u_b.group.id = ?2 AND " +
            "(u_b.firstAppUser.id = ?1 " +
            "OR " +
            "u_b.secondAppUser.id = ?1) ")
    List<UserBalance> findAllByAppUserIdAndGroupId(UUID firstAppUserId, Long groupId);

    @Query(" SELECT u_b " +
            "FROM UserBalance  u_b " +
            "WHERE u_b.group.id = ?3 AND " +
                "((u_b.firstAppUser.id = ?1 AND u_b.secondAppUser.id = ?2) " +
                "OR " +
                "(u_b.firstAppUser.id = ?2 AND u_b.secondAppUser.id = ?1)) ")
    List<UserBalance> findAllByAppUserIdsAndGroupId(UUID firstAppUserId, UUID secondAppUserId, Long groupId);

    @Query(" SELECT u_b " +
            "FROM UserBalance  u_b " +
            "WHERE u_b.group.id = ?3 AND " +
            "u_b.currency = ?4 AND " +
            "((u_b.firstAppUser.id = ?1 AND u_b.secondAppUser.id = ?2) " +
            "OR " +
            "(u_b.firstAppUser.id = ?2 AND u_b.secondAppUser.id = ?1)) ")
    Optional<UserBalance> findByAppUserIdsAndGroupIdAndCurrency(UUID firstAppUserId, UUID secondAppUserId, Long groupId, Currency currency);
}
