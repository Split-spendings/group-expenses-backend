package com.splitspendings.groupexpensesbackend.model;

import com.splitspendings.groupexpensesbackend.model.enums.GroupTheme;
import com.splitspendings.groupexpensesbackend.model.enums.NotificationCategory;
import com.splitspendings.groupexpensesbackend.model.enums.NotificationOption;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "group_membership_settings")
@Getter
@Setter
public class GroupMembershipSettings {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "group_membership_id", foreignKey = @ForeignKey(name = "fk_group_membership_settings"))
    private GroupMembership groupMembership;

    @Enumerated(EnumType.STRING)
    @Column(name = "group_theme", nullable = false, length = GroupTheme.MAX_LENGTH)
    private GroupTheme groupTheme;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_option", nullable = false, length = NotificationOption.MAX_LENGTH)
    private NotificationOption notificationOption;

    @ElementCollection
    @CollectionTable(
            name = "group_membership_notification_category",
            joinColumns = @JoinColumn(name = "group_membership_id"),
            foreignKey = @ForeignKey(name = "fk_notification_category_group_membership"))
    @Enumerated(EnumType.STRING)
    @Column(name = "notification_category", length = NotificationCategory.MAX_LENGTH)
    private Set<NotificationCategory> notificationCategories;
}
