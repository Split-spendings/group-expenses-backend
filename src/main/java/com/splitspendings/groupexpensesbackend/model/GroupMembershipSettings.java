package com.splitspendings.groupexpensesbackend.model;

import com.splitspendings.groupexpensesbackend.model.enums.HistoryOption;
import com.splitspendings.groupexpensesbackend.model.enums.NotificationCategory;
import com.splitspendings.groupexpensesbackend.model.enums.ThemeOption;
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

    @ElementCollection
    @CollectionTable(
            name = "group_notification_category",
            joinColumns = @JoinColumn(name = "group_membership_id"),
            foreignKey = @ForeignKey(name = "fk_notification_category_group"))
    @Enumerated(EnumType.STRING)
    @Column(name = "notification_category", length = NotificationCategory.MAX_LENGTH)
    private Set<NotificationCategory> notificationCategories;

    @Enumerated(EnumType.STRING)
    @Column(name = "history_option", nullable = false, length = HistoryOption.MAX_LENGTH)
    private HistoryOption historyOption;


    @Enumerated(EnumType.STRING)
    @Column(name = "theme_option", nullable = false, length = ThemeOption.MAX_LENGTH)
    private ThemeOption themeOption;
}
