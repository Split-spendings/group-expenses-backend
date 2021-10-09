package com.splitspendings.groupexpensesbackend.repository;

import com.splitspendings.groupexpensesbackend.model.SpendingComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SpendingCommentRepository extends JpaRepository<SpendingComment, Long> {
    @Override
    @Query( "SELECT s_c " +
            "FROM SpendingComment s_c " +
            "LEFT JOIN FETCH s_c.addedByAppUser " +
            "LEFT JOIN FETCH s_c.spending " +
            "WHERE s_c.id = ?1 ")
    Optional<SpendingComment> findById(Long id);
}
