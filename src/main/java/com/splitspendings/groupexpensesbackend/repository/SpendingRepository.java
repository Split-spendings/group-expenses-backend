package com.splitspendings.groupexpensesbackend.repository;

import com.splitspendings.groupexpensesbackend.model.Group;
import com.splitspendings.groupexpensesbackend.model.Spending;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpendingRepository extends JpaRepository<Spending, Long> {

    List<Spending> findAllByAddedByGroupMembershipGroup(Group group);

    @Query( "SELECT s " +
            "FROM Spending s " +
            "LEFT JOIN fetch s.comments " +
            "WHERE s.id = ?1")
    Spending findByIdFetchComments(Long spendingId);
}
