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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
    private final GroupMembershipSettingsRepository groupMembershipSettingsRepository;

    private AppUser appUser1;
    private AppUser appUser2;
    private AppUser appUser3;
    private AppUser adminAppUser1;

    @SuppressWarnings("all")
    private AppUserSettings appUserSettings1;
    @SuppressWarnings("all")
    private AppUserSettings appUserSettings2;
    @SuppressWarnings("all")
    private AppUserSettings appUserSettings3;
    @SuppressWarnings("all")
    private AppUserSettings adminAppUserSettings1;

    private Group group1;
    private Group group2;
    private Group group3;

    private GroupMembership groupMembership1;
    private GroupMembership groupMembership2;
    private GroupMembership groupMembership3;
    private GroupMembership groupMembership4;
    private GroupMembership groupMembership5;
    private GroupMembership groupMembership6;

    @SuppressWarnings("all")
    private GroupMembershipSettings groupMembershipSettings1;
    @SuppressWarnings("all")
    private GroupMembershipSettings groupMembershipSettings2;
    @SuppressWarnings("all")
    private GroupMembershipSettings groupMembershipSettings3;
    @SuppressWarnings("all")
    private GroupMembershipSettings groupMembershipSettings4;
    @SuppressWarnings("all")
    private GroupMembershipSettings groupMembershipSettings5;
    @SuppressWarnings("all")
    private GroupMembershipSettings groupMembershipSettings6;

    private Spending spending1;

    private Spending spending2;

    @SuppressWarnings("all")
    private SpendingComment spendingComment1;
    @SuppressWarnings("all")
    private SpendingComment spendingComment2;

    private ItemCategory itemCategory1;
    @SuppressWarnings("all")
    private ItemCategory itemCategory2;

    private Item item1;

    @SuppressWarnings("all")
    private Share share1;

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
        setUpAppUsers();
        setUpAppUserSettings();
        setUpGroups();
        setUpGroupMembership();
        setUpGroupMembershipSettings();
        setUpGroupInvites();
        setUpSpendings();
        setUpSpendingComments();
        setUpItemCategories();
        setUpItems();
        setUpShares();
    }

    private void setUpShares() {
        share1 = new Share();
        share1.setAmount(new BigDecimal("20.02"));
        share1.setItem(item1);
        share1.setCurrency(Currency.EUR);
        share1.setPaidByGroupMembership(groupMembership1);
        share1.setPaidForGroupMembership(groupMembership6);

        shareRepository.save(share1);
    }

    private void setUpItems() {
        item1 = new Item();
        item1.setItemCategory(itemCategory1);
        item1.setSpending(spending1);
        item1.setTitle("Dummy item 1");
        item1.setPrice(new BigDecimal("30.50"));

        itemRepository.save(item1);
    }

    private void setUpItemCategories() {
        itemCategory1 = new ItemCategory();
        itemCategory1.setGroup(group1);
        itemCategory1.setTitle("Dummy category 1");
        itemCategory1.setCreatedByAppUser(appUser1);

        itemCategory2 = new ItemCategory();
        itemCategory2.setTitle("Default category 1");

        itemCategoryRepository.save(itemCategory1);
        itemCategoryRepository.save(itemCategory2);
    }

    private void setUpSpendingComments() {
        spendingComment1 = new SpendingComment();
        spendingComment1.setSpending(spending1);
        spendingComment1.setMessage("Dummy comment 1");
        spendingComment1.setAddedByAppUser(appUser1);
        spendingComment1.setTimeAdded(ZonedDateTime.of(LocalDateTime.of(2020, 8, 27, 12,34,56), ZoneId.of("+02:00")));

        spendingComment2 = new SpendingComment();
        spendingComment2.setSpending(spending1);
        spendingComment2.setMessage("Dummy comment 2");
        spendingComment2.setAddedByAppUser(appUser2);
        spendingComment2.setTimeAdded(ZonedDateTime.of(LocalDateTime.of(2021, 9, 24, 11,30,34), ZoneId.of("-02:00")));

        spendingCommentRepository.save(spendingComment1);
        spendingCommentRepository.save(spendingComment2);
    }

    private void setUpSpendings() {
        spending1 = new Spending();
        spending1.setAddedByGroupMembership(groupMembership1);
        spending1.setCurrency(Currency.EUR);
        spending1.setTotalAmount(new BigDecimal("123.50"));
        spending1.setTitle("Dummy spending1");

        spending2 = new Spending();
        spending2.setAddedByGroupMembership(groupMembership2);
        spending2.setCurrency(Currency.PLN);
        spending2.setTotalAmount(new BigDecimal("234.56"));
        spending2.setTitle("Dummy spending2");

        spendingRepository.save(spending1);
        spendingRepository.save(spending2);
    }

    private void setUpGroupInvites() {
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
    }

    @SuppressWarnings("all")
    private void setUpGroupMembershipSettings() {
        groupMembershipSettings1 = new GroupMembershipSettings();
        groupMembershipSettings1.setGroupMembership(groupMembership1);
        groupMembershipSettings1.setGroupTheme(GroupTheme.DEFAULT);
        groupMembershipSettings1.setNotificationOption(NotificationOption.SELECTED);
        groupMembershipSettings1.setNotificationCategories(Set.of(NotificationCategory.NEW_SPENDING));

        groupMembershipSettings2 = new GroupMembershipSettings();
        groupMembershipSettings2.setGroupMembership(groupMembership2);
        groupMembershipSettings2.setGroupTheme(GroupTheme.DEFAULT);
        groupMembershipSettings2.setNotificationOption(NotificationOption.SELECTED);
        groupMembershipSettings2.setNotificationCategories(Set.of(NotificationCategory.NEW_SPENDING));

        groupMembershipSettings3 = new GroupMembershipSettings();
        groupMembershipSettings3.setGroupMembership(groupMembership3);
        groupMembershipSettings3.setGroupTheme(GroupTheme.DEFAULT);
        groupMembershipSettings3.setNotificationOption(NotificationOption.ALL);

        groupMembershipSettings4 = new GroupMembershipSettings();
        groupMembershipSettings4.setGroupMembership(groupMembership4);
        groupMembershipSettings4.setGroupTheme(GroupTheme.DEFAULT);
        groupMembershipSettings4.setNotificationOption(NotificationOption.ALL);

        groupMembershipSettings5 = new GroupMembershipSettings();
        groupMembershipSettings5.setGroupMembership(groupMembership5);
        groupMembershipSettings5.setGroupTheme(GroupTheme.DEFAULT);
        groupMembershipSettings5.setNotificationOption(NotificationOption.ALL);

        groupMembershipSettings6 = new GroupMembershipSettings();
        groupMembershipSettings6.setGroupMembership(groupMembership6);
        groupMembershipSettings6.setGroupTheme(GroupTheme.DEFAULT);
        groupMembershipSettings6.setNotificationOption(NotificationOption.ALL);

        groupMembershipSettingsRepository.save(groupMembershipSettings1);
        groupMembershipSettingsRepository.save(groupMembershipSettings2);
        groupMembershipSettingsRepository.save(groupMembershipSettings3);
        groupMembershipSettingsRepository.save(groupMembershipSettings4);
        groupMembershipSettingsRepository.save(groupMembershipSettings5);
        groupMembershipSettingsRepository.save(groupMembershipSettings6);
    }

    @SuppressWarnings("all")
    private void setUpGroupMembership() {
        groupMembership1 = new GroupMembership();
        groupMembership1.setGroup(group1);
        groupMembership1.setAppUser(appUser1);
        groupMembership1.setActive(true);
        groupMembership1.setHasAdminRights(true);
        groupMembership1.setFirstTimeJoined(groupMembership1.getTimeCreated());
        groupMembership1.setLastTimeJoined(groupMembership1.getFirstTimeJoined());

        groupMembership2 = new GroupMembership();
        groupMembership2.setGroup(group2);
        groupMembership2.setAppUser(appUser2);
        groupMembership2.setActive(false);
        groupMembership2.setHasAdminRights(true);
        groupMembership2.setFirstTimeJoined(groupMembership2.getTimeCreated());
        groupMembership2.setLastTimeJoined(groupMembership2.getFirstTimeJoined());
        groupMembership2.setLastTimeLeft(groupMembership2.getFirstTimeJoined().plusWeeks(1));

        groupMembership3 = new GroupMembership();
        groupMembership3.setGroup(group3);
        groupMembership3.setAppUser(appUser3);
        groupMembership3.setActive(true);
        groupMembership3.setHasAdminRights(true);
        groupMembership3.setFirstTimeJoined(groupMembership1.getTimeCreated());
        groupMembership3.setLastTimeJoined(groupMembership1.getFirstTimeJoined());

        groupMembership4 = new GroupMembership();
        groupMembership4.setGroup(group3);
        groupMembership4.setAppUser(appUser1);
        groupMembership4.setActive(false);
        groupMembership4.setHasAdminRights(false);
        groupMembership4.setFirstTimeJoined(groupMembership1.getTimeCreated());
        groupMembership4.setLastTimeJoined(groupMembership1.getFirstTimeJoined());

        groupMembership5 = new GroupMembership();
        groupMembership5.setGroup(group3);
        groupMembership5.setAppUser(appUser2);
        groupMembership5.setActive(true);
        groupMembership5.setHasAdminRights(false);
        groupMembership5.setFirstTimeJoined(groupMembership1.getTimeCreated());
        groupMembership5.setLastTimeJoined(groupMembership1.getFirstTimeJoined());

        groupMembership6 = new GroupMembership();
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
    }

    private void setUpGroups() {
        group1 = groupFactory.generate();
        group1.setOwner(appUser1);
        group1.setPersonal(false);
        group1.setInviteOption(InviteOption.ALL_ACTIVE_MEMBERS);

        group2 = groupFactory.generate();
        group2.setOwner(appUser2);
        group2.setPersonal(true);
        group2.setInviteOption(InviteOption.OWNER_ONLY);

        group3 = groupFactory.generate();
        group3.setOwner(appUser3);
        group3.setPersonal(false);
        group3.setInviteOption(InviteOption.OWNER_ONLY);

        groupRepository.save(group1);
        groupRepository.save(group2);
        groupRepository.save(group3);
    }

    private void setUpAppUserSettings() {
        appUserSettings1 = new AppUserSettings();
        appUserSettings1.setAppUser(appUser1);
        appUserSettings1.setDefaultCurrency(Currency.PLN);
        appUserSettings1.setLanguage(Language.PL);
        appUserSettings1.setTheme(Theme.DARK);
        appUserSettings1.setGroupInviteOption(GroupInviteOption.ANYONE);
        appUserSettings1.setNotificationOption(NotificationOption.ALL);

        appUserSettings2 = new AppUserSettings();
        appUserSettings2.setAppUser(appUser2);
        appUserSettings2.setDefaultCurrency(Currency.USD);
        appUserSettings2.setLanguage(Language.EN);
        appUserSettings2.setTheme(Theme.LIGHT);
        appUserSettings2.setGroupInviteOption(GroupInviteOption.NOBODY);
        appUserSettings2.setNotificationOption(NotificationOption.NONE);

        appUserSettings3 = new AppUserSettings();
        appUserSettings3.setAppUser(appUser3);
        appUserSettings3.setDefaultCurrency(Currency.USD);
        appUserSettings3.setLanguage(Language.EN);
        appUserSettings3.setTheme(Theme.LIGHT);
        appUserSettings3.setGroupInviteOption(GroupInviteOption.ANYONE);
        appUserSettings3.setNotificationOption(NotificationOption.NONE);

        adminAppUserSettings1 = new AppUserSettings();
        adminAppUserSettings1.setAppUser(adminAppUser1);
        adminAppUserSettings1.setDefaultCurrency(Currency.PLN);
        adminAppUserSettings1.setLanguage(Language.PL);
        adminAppUserSettings1.setTheme(Theme.DARK);
        adminAppUserSettings1.setGroupInviteOption(GroupInviteOption.ANYONE);
        adminAppUserSettings1.setNotificationOption(NotificationOption.SELECTED);
        adminAppUserSettings1.setNotificationCategories(Set.of(NotificationCategory.NEW_COMMENT, NotificationCategory.NEW_SPENDING));

        appUserSettingsRepository.save(appUserSettings1);
        appUserSettingsRepository.save(appUserSettings2);
        appUserSettingsRepository.save(appUserSettings3);
        appUserSettingsRepository.save(adminAppUserSettings1);
    }

    @SuppressWarnings("all")
    private void setUpAppUsers() throws Exception{
        var usersIdMap = loadAppUserIds(USERS_PATH);
        if(usersIdMap.size() < 2) {
            throw new Exception("not enough test user IDs to load data");
        }

        var adminsIdMap = loadAppUserIds(ADMINS_PATH);
        if(adminsIdMap.size() < 1) {
            throw new Exception("not enough test admin IDs to load data");
        }

        appUser1 = appUserFactory.generate();
        appUser2 = appUserFactory.generate();
        appUser3 = appUserFactory.generate();
        adminAppUser1 = appUserFactory.generate();

        appUser1.setId(usersIdMap.get(ID + 1));
        appUser2.setId(usersIdMap.get(ID + 2));
        appUser3.setId(usersIdMap.get(ID + 3));
        adminAppUser1.setId(adminsIdMap.get(ID + 1));
    }
}
