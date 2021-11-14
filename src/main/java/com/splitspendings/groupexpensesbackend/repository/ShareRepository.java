package com.splitspendings.groupexpensesbackend.repository;

import com.splitspendings.groupexpensesbackend.model.Share;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface ShareRepository extends JpaRepository<Share, Long> {

    @Query("SELECT s FROM Share s WHERE s.paidByGroupMembership.group.id = ?1")
    Set<Share> findAllByGroupId(Long groupId);
}
