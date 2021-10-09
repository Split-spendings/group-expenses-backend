package com.splitspendings.groupexpensesbackend.repository;

import com.splitspendings.groupexpensesbackend.model.GroupMembershipSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMembershipSettingsRepository extends JpaRepository<GroupMembershipSettings, Long> {

}
