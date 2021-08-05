package com.splitspendings.groupexpensesbackend.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "app_user",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_app_user_login_name",
                        columnNames = {"login_name"})})
@Getter
@Setter
public class AppUser {

    public static final int NAMES_MIN_LENGTH = 1;
    public static final int NAMES_MAX_LENGTH = 50;
    public static final int EMAIL_MIN_LENGTH = 3;
    public static final int EMAIL_MAX_LENGTH = 320;

    @Id
    private UUID id;

    @Column(name = "login_name", nullable = false, length = NAMES_MAX_LENGTH)
    private String loginName;

    @Column(name = "email", nullable = false, length = EMAIL_MAX_LENGTH)
    private String email;

    @Column(name = "first_name", nullable = false, length = NAMES_MAX_LENGTH)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = NAMES_MAX_LENGTH)
    private String lastName;

    @Column(name = "time_registered", nullable = false)
    private ZonedDateTime timeRegistered = ZonedDateTime.now();

    @ManyToMany
    @JoinTable(
            name = "blocked_user",
            joinColumns = {@JoinColumn(name = "blocked_by_app_user_id", foreignKey = @ForeignKey(name = "fk_blocked_by_app_user"))},
            inverseJoinColumns = {@JoinColumn(name = "blocked_app_user_id", foreignKey = @ForeignKey(name = "fk_blocked_app_user"))}
    )
    private Set<AppUser> blockedAppUsers;

    @ManyToMany
    @JoinTable(
            name = "friendship",
            joinColumns = {@JoinColumn(name = "friend_app_user_id", foreignKey = @ForeignKey(name = "fk_friend_1"))},
            inverseJoinColumns = {@JoinColumn(name = "friend2_app_user_id", foreignKey = @ForeignKey(name = "fk_friend_2"))}
    )
    private Set<AppUser> friends;

    @ManyToMany(mappedBy = "blockedAppUsers")
    private Set<AppUser> blockedByAppUsers;

    @ManyToMany(mappedBy = "friends")
    private Set<AppUser> friendsReverse;

    @OneToMany(mappedBy = "owner")
    private Set<Group> ownedGroups;

    @OneToMany(mappedBy = "notifiedAppUser")
    private Set<Notification> notifications;

    @OneToMany(mappedBy = "invitedAppUser")
    private Set<GroupInvite> groupInvitesReceived;

    @OneToMany(mappedBy = "addedByAppUser")
    private Set<SpendingComment> commentsLeft;

    @OneToMany(mappedBy = "createdByAppUser")
    private Set<ItemCategory> createdItemCategories;

    @OneToMany(mappedBy = "paidByAppUser")
    private Set<Share> paidShares;

    @OneToMany(mappedBy = "paidForAppUser")
    private Set<Share> debtShares;

    @OneToOne(mappedBy = "appUser")
    private AppUserSettings appUserSettings;
}

