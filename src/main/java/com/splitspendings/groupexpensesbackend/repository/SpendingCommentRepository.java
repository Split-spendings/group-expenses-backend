package com.splitspendings.groupexpensesbackend.repository;

import com.splitspendings.groupexpensesbackend.model.SpendingComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpendingCommentRepository extends JpaRepository<SpendingComment, Long> {
}
