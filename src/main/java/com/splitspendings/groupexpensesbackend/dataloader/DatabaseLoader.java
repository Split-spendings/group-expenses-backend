package com.splitspendings.groupexpensesbackend.dataloader;

import com.splitspendings.groupexpensesbackend.dataloader.factory.AppUserFactory;
import com.splitspendings.groupexpensesbackend.dataloader.factory.GroupFactory;
import com.splitspendings.groupexpensesbackend.model.*;
import com.splitspendings.groupexpensesbackend.model.enums.*;
import com.splitspendings.groupexpensesbackend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
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
    private final GroupFactory groupFactory;

    private final AppUserSettingsRepository appUserSettingsRepository;
    private final GroupRepository groupRepository;
    private final GroupMembershipRepository groupMembershipRepository;
    private final GroupInviteRepository groupInviteRepository;
    private final SpendingRepository spendingRepository;
    private final SpendingCommentRepository spendingCommentRepository;
    private final ItemCategoryRepository itemCategoryRepository;
    private final ItemRepository itemRepository;
    private final ShareRepository shareRepository;

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
        AppUser appUser3 = appUserFactory.generate();
        AppUser adminAppUser1 = appUserFactory.generate();

        appUser1.setId(usersIdMap.get(ID + 1));
        appUser2.setId(usersIdMap.get(ID + 2));
        appUser3.setId(usersIdMap.get(ID + 3));
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

        AppUserSettings appUserSettings3 = new AppUserSettings();
        appUserSettings3.setAppUser(appUser3);
        appUserSettings3.setDefaultCurrency(Currency.USD);
        appUserSettings3.setLanguage(Language.EN);
        appUserSettings3.setTheme(Theme.LIGHT);
        appUserSettings3.setGroupInviteOption(GroupInviteOption.ANYONE);
        appUserSettings3.setNotificationOption(NotificationOption.NONE);

        AppUserSettings adminAppUserSettings1 = new AppUserSettings();
        adminAppUserSettings1.setAppUser(adminAppUser1);
        adminAppUserSettings1.setDefaultCurrency(Currency.PLN);
        adminAppUserSettings1.setLanguage(Language.PL);
        adminAppUserSettings1.setTheme(Theme.DARK);
        adminAppUserSettings1.setGroupInviteOption(GroupInviteOption.ANYONE);
        adminAppUserSettings1.setNotificationOption(NotificationOption.ALL);

        appUserSettingsRepository.save(appUserSettings1);
        appUserSettingsRepository.save(appUserSettings2);
        appUserSettingsRepository.save(appUserSettings3);
        appUserSettingsRepository.save(adminAppUserSettings1);


        Group group1 = groupFactory.generate();
        group1.setOwner(appUser1);
        group1.setPersonal(false);
        group1.setInviteOption(InviteOption.ALL_ACTIVE_MEMBERS);

        Group group2 = groupFactory.generate();
        group2.setOwner(appUser2);
        group2.setPersonal(true);
        group2.setInviteOption(InviteOption.OWNER_ONLY);

        Group group3 = groupFactory.generate();
        group3.setOwner(appUser3);
        group3.setPersonal(false);
        group3.setInviteOption(InviteOption.OWNER_ONLY);

        groupRepository.save(group1);
        groupRepository.save(group2);
        groupRepository.save(group3);


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

        GroupMembership groupMembership3 = new GroupMembership();
        groupMembership3.setGroup(group3);
        groupMembership3.setAppUser(appUser3);
        groupMembership3.setActive(true);
        groupMembership3.setHasAdminRights(true);
        groupMembership3.setFirstTimeJoined(groupMembership1.getTimeCreated());
        groupMembership3.setLastTimeJoined(groupMembership1.getFirstTimeJoined());

        GroupMembership groupMembership4 = new GroupMembership();
        groupMembership4.setGroup(group3);
        groupMembership4.setAppUser(appUser1);
        groupMembership4.setActive(false);
        groupMembership4.setHasAdminRights(false);
        groupMembership4.setFirstTimeJoined(groupMembership1.getTimeCreated());
        groupMembership4.setLastTimeJoined(groupMembership1.getFirstTimeJoined());

        GroupMembership groupMembership5 = new GroupMembership();
        groupMembership5.setGroup(group3);
        groupMembership5.setAppUser(appUser2);
        groupMembership5.setActive(true);
        groupMembership5.setHasAdminRights(false);
        groupMembership5.setFirstTimeJoined(groupMembership1.getTimeCreated());
        groupMembership5.setLastTimeJoined(groupMembership1.getFirstTimeJoined());

        GroupMembership groupMembership6 = new GroupMembership();
        groupMembership6.setGroup(group1);
        groupMembership6.setAppUser(appUser3);
        groupMembership6.setActive(true);
        groupMembership6.setHasAdminRights(false);
        groupMembership6.setFirstTimeJoined(groupMembership1.getTimeCreated());
        groupMembership6.setLastTimeJoined(groupMembership1.getFirstTimeJoined());

        groupMembershipRepository.save(groupMembership1);
        groupMembershipRepository.save(groupMembership2);
        groupMembershipRepository.save(groupMembership3);
        groupMembershipRepository.save(groupMembership4);
        groupMembershipRepository.save(groupMembership5);
        groupMembershipRepository.save(groupMembership6);


        GroupInvite groupInvite1 = new GroupInvite();
        groupInvite1.setMessage("hello");
        groupInvite1.setInvitedAppUser(appUser2);
        groupInvite1.setInvitedByGroupMembership(groupMembership1);

        GroupInvite groupInvite2 = new GroupInvite();
        groupInvite2.setInvitedAppUser(appUser1);
        groupInvite2.setInvitedByGroupMembership(groupMembership2);

        GroupInvite groupInvite3 = new GroupInvite();
        groupInvite3.setInvitedAppUser(appUser1);
        groupInvite3.setInvitedByGroupMembership(groupMembership3);

        GroupInvite groupInvite4 = new GroupInvite();
        groupInvite4.setInvitedAppUser(appUser2);
        groupInvite4.setInvitedByGroupMembership(groupMembership3);

        GroupInvite groupInvite5 = new GroupInvite();
        groupInvite5.setInvitedAppUser(appUser2);
        groupInvite5.setInvitedByGroupMembership(groupMembership6);

        groupInviteRepository.save(groupInvite1);
        groupInviteRepository.save(groupInvite2);
        groupInviteRepository.save(groupInvite3);
        groupInviteRepository.save(groupInvite4);
        groupInviteRepository.save(groupInvite5);

        Spending spending1 = new Spending();
        spending1.setGroupMembership(groupMembership1);
        spending1.setCurrency(Currency.EUR);
        spending1.setExchangeRate(new BigDecimal("31.02"));
        spending1.setTotalSpending(new BigDecimal("123.50"));
        spending1.setTitle("Dummy spending1");
        spendingRepository.save(spending1);

        SpendingComment spendingComment1 = new SpendingComment();
        spendingComment1.setSpending(spending1);
        spendingComment1.setMessage("Dummy comment 1");
        spendingComment1.setAppUser(appUser1);
        spendingCommentRepository.save(spendingComment1);

        ItemCategory itemCategory1 = new ItemCategory();
        itemCategory1.setGroup(group1);
        itemCategory1.setTitle("Dummy category 1");
        itemCategory1.setCreatedBy(appUser1);

        ItemCategory itemCategory2 = new ItemCategory();
        itemCategory2.setTitle("Default category 1");

        itemCategoryRepository.save(itemCategory1);
        itemCategoryRepository.save(itemCategory2);

        Item item1 = new Item();
        item1.setItemCategory(itemCategory1);
        item1.setSpending(spending1);
        item1.setMessage("Dummy item 1");
        item1.setPrice(new BigDecimal("30.50"));

        itemRepository.save(item1);

        Share share1 = new Share();
        share1.setAmount(new BigDecimal("20.02"));
        share1.setItem(item1);
        share1.setPaidBy(appUser1);
        share1.setPaidFor(appUser2);

        shareRepository.save(share1);
    }
}
