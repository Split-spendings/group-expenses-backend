package com.splitspendings.groupexpensesbackend.repository;

import com.splitspendings.groupexpensesbackend.model.GroupMembershipSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupMembershipSettingsRepository extends JpaRepository<GroupMembershipSettings, Long> {
    @Override
    Optional<GroupMembershipSettings> findById(Long id);
}
