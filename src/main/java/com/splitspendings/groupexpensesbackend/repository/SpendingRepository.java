package com.splitspendings.groupexpensesbackend.repository;

import com.splitspendings.groupexpensesbackend.model.Spending;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpendingRepository extends JpaRepository<Spending, Long> {
}
