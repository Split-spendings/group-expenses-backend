package com.splitspendings.groupexpensesbackend.dataloader;

import com.splitspendings.groupexpensesbackend.model.AppUser;
import com.splitspendings.groupexpensesbackend.model.AppUserSettings;
import com.splitspendings.groupexpensesbackend.model.Group;
import com.splitspendings.groupexpensesbackend.model.GroupMembership;
import com.splitspendings.groupexpensesbackend.model.enums.GroupInviteOption;
import com.splitspendings.groupexpensesbackend.model.enums.InviteOption;
import com.splitspendings.groupexpensesbackend.model.enums.NotificationOption;
import com.splitspendings.groupexpensesbackend.repository.AppUserRepository;
import com.splitspendings.groupexpensesbackend.repository.AppUserSettingsRepository;
import com.splitspendings.groupexpensesbackend.repository.GroupMembershipRepository;
import com.splitspendings.groupexpensesbackend.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseLoader implements CommandLineRunner {

    private final AppUserRepository appUserRepository;
    private final AppUserSettingsRepository appUserSettingsRepository;
    private final GroupRepository groupRepository;
    private final GroupMembershipRepository groupMembershipRepository;

    @Override
    public void run(String... args) throws Exception {

        AppUser appUser1 = new AppUser();
        appUser1.setLoginName("login_name_1");
        appUser1.setEmail("email_1@test");
        appUser1.setFirstName("first_name_1");
        appUser1.setLastName("last_name_1");

        AppUser appUser2 = new AppUser();
        appUser2.setLoginName("login_name_2");
        appUser2.setEmail("email_2@test");
        appUser2.setFirstName("first_name_2");
        appUser2.setLastName("last_name_2");


        AppUserSettings appUserSettings1 = new AppUserSettings();
        appUserSettings1.setAppUser(appUser1);
        appUserSettings1.setDefaultCurrencyCode("PLN");
        appUserSettings1.setLanguageCode("PL");
        appUserSettings1.setTheme("dark");
        appUserSettings1.setGroupInviteOption(GroupInviteOption.ANYONE);
        appUserSettings1.setNotificationOption(NotificationOption.ALL);

        AppUserSettings appUserSettings2 = new AppUserSettings();
        appUserSettings2.setAppUser(appUser2);
        appUserSettings2.setDefaultCurrencyCode("USD");
        appUserSettings2.setLanguageCode("EN");
        appUserSettings2.setTheme("light");
        appUserSettings2.setGroupInviteOption(GroupInviteOption.NOBODY);
        appUserSettings2.setNotificationOption(NotificationOption.NONE);

        appUserSettingsRepository.save(appUserSettings1);
        appUserSettingsRepository.save(appUserSettings2);


        Group group1 = new Group();
        group1.setName("group_1");
        group1.setOwner(appUser1);
        group1.setPersonal(false);
        group1.setInviteOption(InviteOption.ALL_ACTIVE_MEMBERS);

        Group group2 = new Group();
        group2.setName("group_2");
        group2.setOwner(appUser2);
        group2.setPersonal(true);
        group2.setInviteOption(InviteOption.OWNER_ONLY);

        groupRepository.save(group1);
        groupRepository.save(group2);

        GroupMembership groupMembership1 = new GroupMembership();
        groupMembership1.setGroup(group1);
        groupMembership1.setAppUser(appUser1);
        groupMembership1.setActive(true);
        groupMembership1.setHasAdminRights(true);
        groupMembership1.setFirstTimeJoined(groupMembership1.getTimeCreated());
        groupMembership1.setLastTimeJoined(groupMembership1.getFirstTimeJoined());

        GroupMembership groupMembership2 = new GroupMembership();
        groupMembership2.setGroup(group2);
        groupMembership2.setAppUser(appUser2);
        groupMembership2.setActive(false);
        groupMembership2.setHasAdminRights(true);
        groupMembership2.setFirstTimeJoined(groupMembership2.getTimeCreated());
        groupMembership2.setLastTimeJoined(groupMembership2.getFirstTimeJoined());
        groupMembership2.setLastTimeLeft(groupMembership2.getFirstTimeJoined().plusWeeks(1));

        groupMembershipRepository.save(groupMembership1);
        groupMembershipRepository.save(groupMembership2);
    }
}
