package com.splitspendings.groupexpensesbackend.model;

import com.splitspendings.groupexpensesbackend.model.enums.Currency;
import com.splitspendings.groupexpensesbackend.model.enums.GroupInviteOption;
import com.splitspendings.groupexpensesbackend.model.enums.Language;
import com.splitspendings.groupexpensesbackend.model.enums.NotificationCategory;
import com.splitspendings.groupexpensesbackend.model.enums.NotificationOption;
import com.splitspendings.groupexpensesbackend.model.enums.Theme;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "app_user_settings")
@Getter
@Setter
public class AppUserSettings {

    @Id
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "app_user_id", foreignKey = @ForeignKey(name = "fk_app_user_settings"))
    private AppUser appUser;

    @Enumerated(EnumType.STRING)
    @Column(name = "language_code", nullable = false, length = Language.MAX_LENGTH)
    private Language language;

    @Enumerated(EnumType.STRING)
    @Column(name = "theme", nullable = false, length = Theme.MAX_LENGTH)
    private Theme theme;

    @Enumerated(EnumType.STRING)
    @Column(name = "default_currency_code", nullable = false, length = Currency.MAX_LENGTH)
    private Currency defaultCurrency;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_option", nullable = false, length = NotificationOption.MAX_LENGTH)
    private NotificationOption notificationOption;

    @Enumerated(EnumType.STRING)
    @Column(name = "group_invite_option", nullable = false, length = GroupInviteOption.MAX_LENGTH)
    private GroupInviteOption groupInviteOption;

    @ElementCollection
    @CollectionTable(
            name = "app_user_notification_category",
            joinColumns = @JoinColumn(name = "app_user_id"),
            foreignKey = @ForeignKey(name = "fk_notification_category_app_user"))
    @Enumerated(EnumType.STRING)
    @Column(name = "notification_category", length = NotificationCategory.MAX_LENGTH)
    private Set<NotificationCategory> notificationCategories;
}
