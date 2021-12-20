package com.splitspendings.groupexpensesbackend.repository;

import com.splitspendings.groupexpensesbackend.model.AppUser;
import com.splitspendings.groupexpensesbackend.model.Group;
import com.splitspendings.groupexpensesbackend.model.GroupMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupMembershipRepository extends JpaRepository<GroupMembership, Long> {

    @Query("select gm.group from GroupMembership gm where gm.appUser.id = :app_user_id and gm.active=:isActive")
    List<Group> findAllByAppUserIdAndIsActive(@Param("app_user_id") UUID appUserId, @Param("isActive") boolean isActive);

    @Query("select gm.group from GroupMembership gm where gm.appUser.id = :app_user_id")
    List<Group> findAllByAppUserId(@Param("app_user_id") UUID appUserId);

    @Query("select gm from GroupMembership gm where gm.group.id = :group_id and gm.appUser.id = :app_user_id and gm.active=true")
    Optional<GroupMembership> queryByGroupIdAndAppUserIdAndActiveTrue(@Param("group_id") Long groupId, @Param("app_user_id") UUID appUserId);

    @Query("select gm.appUser from GroupMembership gm where gm.group.id = :group_id and gm.active=true")
    List<AppUser> queryActiveMembersOfGroupWithId(@Param("group_id") Long groupId);

    Optional<GroupMembership> findByGroupAndAppUser(Group group, AppUser appUser);
}
