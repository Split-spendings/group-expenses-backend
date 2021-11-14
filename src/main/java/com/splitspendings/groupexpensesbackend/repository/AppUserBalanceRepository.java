package com.splitspendings.groupexpensesbackend.repository;

import com.splitspendings.groupexpensesbackend.model.Group;
import com.splitspendings.groupexpensesbackend.model.UserBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserBalanceRepository extends JpaRepository<UserBalance, Long> {
    void deleteAllByGroup(Group group);
}
