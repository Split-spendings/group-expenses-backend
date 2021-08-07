package com.splitspendings.groupexpensesbackend.repository;

import com.splitspendings.groupexpensesbackend.model.SpendingLimit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpendingLimitRepository extends JpaRepository<SpendingLimit, Long> {
}
