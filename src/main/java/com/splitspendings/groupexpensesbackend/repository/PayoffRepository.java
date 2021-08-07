package com.splitspendings.groupexpensesbackend.repository;

import com.splitspendings.groupexpensesbackend.model.Payoff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayoffRepository extends JpaRepository<Payoff, Long> {
}
