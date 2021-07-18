package com.splitspendings.groupexpensesbackend.dataloader;

import com.splitspendings.groupexpensesbackend.dataloader.factory.AppUserFactory;
import com.splitspendings.groupexpensesbackend.model.AppUser;
import com.splitspendings.groupexpensesbackend.model.AppUserSettings;
import com.splitspendings.groupexpensesbackend.model.Group;
import com.splitspendings.groupexpensesbackend.model.GroupMembership;
import com.splitspendings.groupexpensesbackend.model.enums.*;
import com.splitspendings.groupexpensesbackend.repository.AppUserRepository;
import com.splitspendings.groupexpensesbackend.repository.AppUserSettingsRepository;
import com.splitspendings.groupexpensesbackend.repository.GroupMembershipRepository;
import com.splitspendings.groupexpensesbackend.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DatabaseLoader implements CommandLineRunner {

    private static final String ID = "id";
    private static final String USERS_PATH = "gitignored/test_user_IDs.txt";
    private static final String ADMINS_PATH = "gitignored/test_admin_IDs.txt";

    private final AppUserFactory appUserFactory;

    private final AppUserRepository appUserRepository;
    private final AppUserSettingsRepository appUserSettingsRepository;
    private final GroupRepository groupRepository;
    private final GroupMembershipRepository groupMembershipRepository;

    private Map<String, UUID> loadAppUserIds(String path) {
        Map<String, UUID> idMap = new HashMap<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line = reader.readLine();
            int nextIdIndex = 1;
            while (line != null) {
                idMap.put(ID + nextIdIndex++, UUID.fromString(line));
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return idMap;
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {

        var usersIdMap = loadAppUserIds(USERS_PATH);
        if(usersIdMap.size() < 2) {
            throw new Exception("not enough test user IDs to load data");
        }

        var adminsIdMap = loadAppUserIds(ADMINS_PATH);
        if(adminsIdMap.size() < 1) {
            throw new Exception("not enough test admin IDs to load data");
        }

        AppUser appUser1 = appUserFactory.generate();
        AppUser appUser2 = appUserFactory.generate();
        AppUser adminAppUser1 = appUserFactory.generate();

        appUser1.setId(usersIdMap.get(ID + 1));
        appUser2.setId(usersIdMap.get(ID + 2));
        adminAppUser1.setId(adminsIdMap.get(ID + 1));


        AppUserSettings appUserSettings1 = new AppUserSettings();
        appUserSettings1.setAppUser(appUser1);
        appUserSettings1.setDefaultCurrency(Currency.PLN);
        appUserSettings1.setLanguage(Language.PL);
        appUserSettings1.setTheme(Theme.DARK);
        appUserSettings1.setGroupInviteOption(GroupInviteOption.ANYONE);
        appUserSettings1.setNotificationOption(NotificationOption.ALL);

        AppUserSettings appUserSettings2 = new AppUserSettings();
        appUserSettings2.setAppUser(appUser2);
        appUserSettings2.setDefaultCurrency(Currency.USD);
        appUserSettings2.setLanguage(Language.EN);
        appUserSettings2.setTheme(Theme.LIGHT);
        appUserSettings2.setGroupInviteOption(GroupInviteOption.NOBODY);
        appUserSettings2.setNotificationOption(NotificationOption.NONE);

        AppUserSettings adminAppUserSettings1 = new AppUserSettings();
        adminAppUserSettings1.setAppUser(adminAppUser1);
        adminAppUserSettings1.setDefaultCurrency(Currency.PLN);
        adminAppUserSettings1.setLanguage(Language.PL);
        adminAppUserSettings1.setTheme(Theme.DARK);
        adminAppUserSettings1.setGroupInviteOption(GroupInviteOption.ANYONE);
        adminAppUserSettings1.setNotificationOption(NotificationOption.ALL);

        appUserSettingsRepository.save(appUserSettings1);
        appUserSettingsRepository.save(appUserSettings2);
        appUserSettingsRepository.save(adminAppUserSettings1);


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
