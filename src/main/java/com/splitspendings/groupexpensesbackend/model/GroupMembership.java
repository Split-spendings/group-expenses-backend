package com.splitspendings.groupexpensesbackend.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Table(name = "group_membership",
        uniqueConstraints = {@UniqueConstraint(
                name = "unique_membership_group_user",
                columnNames = {"group_id", "app_user_id"})})
@Getter
@Setter
public class GroupMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "has_admin_rights", nullable = false)
    private Boolean hasAdminRights;

    @Column(name = "time_created", nullable = false)
    private ZonedDateTime timeCreated = ZonedDateTime.now();

    @Column(name = "first_time_joined")
    private ZonedDateTime firstTimeJoined;

    @Column(name = "last_time_joined")
    private ZonedDateTime lastTimeJoined;

    @Column(name = "last_time_left")
    private ZonedDateTime lastTimeLeft;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", foreignKey = @ForeignKey(name = "fk_membership_group"))
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "app_user_id", foreignKey = @ForeignKey(name = "fk_membership_user"))
    private AppUser appUser;

    @OneToMany(mappedBy = "invitedByGroupMembership")
    private Set<GroupInvite> invitesSent;

    @OneToMany(mappedBy = "addedByGroupMembership")
    private Set<Spending> spendings;

    @OneToOne(mappedBy = "groupMembership", optional = false)
    private GroupMembershipSettings groupMembershipSettings;
}
