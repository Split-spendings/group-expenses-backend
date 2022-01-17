package com.splitspendings.groupexpensesbackend.repository;

import com.splitspendings.groupexpensesbackend.model.AppUser;
import com.splitspendings.groupexpensesbackend.model.Group;
import com.splitspendings.groupexpensesbackend.model.GroupMembership;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupMembershipRepository extends JpaRepository<GroupMembership, Long> {

    @Query("select gm from GroupMembership gm join fetch gm.group g where gm.appUser.id = :app_user_id and gm.active=:isActive")
    List<GroupMembership> findAllByAppUserIdAndIsActive(@Param("app_user_id") UUID appUserId, @Param("isActive") boolean isActive);

    @Query("select gm from GroupMembership gm join fetch gm.group g where gm.appUser.id = :app_user_id")
    List<GroupMembership> findAllByAppUserId(@Param("app_user_id") UUID appUserId);

    @Query("select gm from GroupMembership gm where gm.group.id = :group_id and gm.appUser.id = :app_user_id and gm.active=true")
    Optional<GroupMembership> queryByGroupIdAndAppUserIdAndActiveTrue(@Param("group_id") Long groupId, @Param("app_user_id") UUID appUserId);

    @Query("select gm.appUser from GroupMembership gm where gm.group.id = :group_id and gm.active=true")
    List<AppUser> queryActiveMembersOfGroupWithId(@Param("group_id") Long groupId);

    @Query("select gm from GroupMembership gm where gm.group.id = :group_id and gm.active=true")
    List<GroupMembership> getActiveMembersOfGroupWithId(@Param("group_id") Long groupId);

    Optional<GroupMembership> findByGroupAndAppUser(Group group, AppUser appUser);

    Optional<GroupMembership> findByInviteCode(String inviteCode);

    @Query( "SELECT g_m.inviteCode " +
            "FROM GroupMembership g_m " +
            "WHERE g_m.group.id = :groupId AND g_m.appUser.id = :app_user_id ")
    Optional<String> findInviteCodeByAppUserIdAndGroupId(@Param("app_user_id") UUID appUserId, @Param("group_id") Long groupId);
}
