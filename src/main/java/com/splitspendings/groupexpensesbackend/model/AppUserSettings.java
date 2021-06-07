package com.splitspendings.groupexpensesbackend.model;

import com.splitspendings.groupexpensesbackend.model.enums.GroupInviteOption;
import com.splitspendings.groupexpensesbackend.model.enums.NotificationCategory;
import com.splitspendings.groupexpensesbackend.model.enums.NotificationOption;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "app_user_settings")
@Getter
@Setter
public class AppUserSettings {

    public static final int LANGUAGE_CODE_MIN_LENGTH = 1;
    public static final int LANGUAGE_CODE_MAX_LENGTH = 5;
    public static final int THEME_MIN_LENGTH = 1;
    public static final int THEME_MAX_LENGTH = 50;
    public static final int CURRENCY_CODE_MIN_LENGTH = 2;
    public static final int CURRENCY_CODE_MAX_LENGTH = 4;

    @Id
    @Column(name = "app_user_id")
    private Long id;

    @Column(name = "language_code", nullable = false, length = LANGUAGE_CODE_MAX_LENGTH)
    private String languageCode;

    @Column(name = "theme", nullable = false, length = THEME_MAX_LENGTH)
    private String theme;

    @Column(name = "default_currency_code", nullable = false, length = CURRENCY_CODE_MAX_LENGTH)
    private String defaultCurrencyCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_option", nullable = false, length = NotificationOption.MAX_LENGTH)
    private NotificationOption notificationOption;

    @Enumerated(EnumType.STRING)
    @Column(name = "group_invite_option", nullable = false, length = GroupInviteOption.MAX_LENGTH)
    private GroupInviteOption groupInviteOption;

    @ElementCollection
    @CollectionTable(name="app_user_notification_category", foreignKey = @ForeignKey(name = "fk_notification_category_app_user"))
    @Enumerated(EnumType.STRING)
    @Column(name = "notification_category", length = NotificationCategory.MAX_LENGTH)
    private Set<NotificationCategory> notificationCategories;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "app_user_id", foreignKey = @ForeignKey(name = "fk_app_user_settings"))
    private AppUser appUser;
}
