package com.splitspendings.groupexpensesbackend.repository;

import com.splitspendings.groupexpensesbackend.model.Group;
import com.splitspendings.groupexpensesbackend.model.Spending;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpendingRepository extends JpaRepository<Spending, Long> {

    List<Spending> findAllByAddedByGroupMembershipGroup(Group group);

    @Query( "SELECT s " +
            "FROM Spending s " +
            "LEFT JOIN fetch s.comments " +
            "WHERE s.id = ?1 " +
            "AND s.addedByGroupMembership.appUser.id = ?2 " +
            "AND s.addedByGroupMembership.active = true")
    Optional<Spending> findByIdFetchComments(Long spendingId, UUID userId);
}
