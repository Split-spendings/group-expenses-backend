package com.splitspendings.groupexpensesbackend.repository;

import com.splitspendings.groupexpensesbackend.model.Group;
import com.splitspendings.groupexpensesbackend.model.Spending;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpendingRepository extends JpaRepository<Spending, Long> {

    List<Spending> findAllByAddedByGroupMembershipGroup(Group group);
}
