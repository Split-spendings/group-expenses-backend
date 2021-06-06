package com.splitspendings.groupexpensesbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class AppUserSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "app_user_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "app_user_id")
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "default_currency_code_id", nullable = false)
    private Currency defaultCurrency;

    @OneToMany(mappedBy = "userSettings", fetch = FetchType.LAZY)
    private Set<UserNotificationCategory> notificationCategorySet;


    @Column(nullable = false)
    private String languageCode;

    @Column(nullable = false)
    private String userTheme;

    @Column(nullable = false)
    private String userNotificationOption;

    @Column(nullable = false)
    private String userGroupInviteOption;
}
