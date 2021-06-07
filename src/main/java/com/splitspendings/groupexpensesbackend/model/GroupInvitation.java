package com.splitspendings.groupexpensesbackend.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "group_invitation",
        uniqueConstraints = {@UniqueConstraint(
                name = "unique_group_invitation_invited_user_invited_by",
                columnNames = {"invited_app_user_id", "invited_by_group_membership_id"})})
@Getter
@Setter
public class GroupInvitation {

    public static final int MESSAGE_MAX_LENGTH = 320;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message", length = MESSAGE_MAX_LENGTH)
    private String message;

    @Column(name = "time_created", nullable = false)
    private ZonedDateTime timeCreated = ZonedDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="invited_app_user_id", foreignKey = @ForeignKey(name = "fk_group_invitation_invited_user"))
    private AppUser invitedAppUser;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="invited_by_group_membership_id", foreignKey = @ForeignKey(name = "fk_group_invitation_invited_by"))
    private GroupMembership groupMembership;
}
