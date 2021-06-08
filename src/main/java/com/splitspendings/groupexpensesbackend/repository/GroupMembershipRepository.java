package com.splitspendings.groupexpensesbackend.repository;

import com.splitspendings.groupexpensesbackend.model.Group;
import com.splitspendings.groupexpensesbackend.model.GroupMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface GroupMembershipRepository extends JpaRepository<GroupMembership, Long> {

    @Query("select gm.group from GroupMembership gm where gm.appUser.id = :app_user_id and gm.active=true")
    List<Group> queryGroupsWithAppUserActiveMembership(@Param("app_user_id") UUID appUserId);
}
