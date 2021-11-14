package com.splitspendings.groupexpensesbackend.repository;

import com.splitspendings.groupexpensesbackend.model.Payoff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface PayoffRepository extends JpaRepository<Payoff, Long> {
    @Query("SELECT p FROM Payoff p WHERE p.group.id = ?1")
    Set<Payoff> findAllByGroupId(Long groupId);
}
