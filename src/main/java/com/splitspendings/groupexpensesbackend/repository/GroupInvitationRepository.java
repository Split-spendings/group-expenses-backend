package com.splitspendings.groupexpensesbackend.repository;

import com.splitspendings.groupexpensesbackend.model.GroupInvite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupInvitationRepository extends JpaRepository<GroupInvite, Long> {
}
