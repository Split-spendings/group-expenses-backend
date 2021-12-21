package com.splitspendings.groupexpensesbackend.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
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

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "groupMembership", optional = false)
    private GroupMembershipSettings groupMembershipSettings;

    @OneToMany(mappedBy = "paidByGroupMembership")
    private Set<Share> paidShares;

    @OneToMany(mappedBy = "paidForGroupMembership")
    private Set<Share> debtShares;
}
