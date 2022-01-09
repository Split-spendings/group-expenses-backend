package com.splitspendings.groupexpensesbackend.dataloader;

import com.splitspendings.groupexpensesbackend.dataloader.factory.AppUserFactory;
import com.splitspendings.groupexpensesbackend.dataloader.factory.GroupFactory;
import com.splitspendings.groupexpensesbackend.model.AppUser;
import com.splitspendings.groupexpensesbackend.model.AppUserSettings;
import com.splitspendings.groupexpensesbackend.model.Group;
import com.splitspendings.groupexpensesbackend.model.GroupInvite;
import com.splitspendings.groupexpensesbackend.model.GroupMembership;
import com.splitspendings.groupexpensesbackend.model.GroupMembershipSettings;
import com.splitspendings.groupexpensesbackend.model.Item;
import com.splitspendings.groupexpensesbackend.model.ItemCategory;
import com.splitspendings.groupexpensesbackend.model.Payoff;
import com.splitspendings.groupexpensesbackend.model.Share;
import com.splitspendings.groupexpensesbackend.model.Spending;
import com.splitspendings.groupexpensesbackend.model.SpendingComment;
import com.splitspendings.groupexpensesbackend.model.enums.Currency;
import com.splitspendings.groupexpensesbackend.model.enums.GroupInviteOption;
import com.splitspendings.groupexpensesbackend.model.enums.GroupTheme;
import com.splitspendings.groupexpensesbackend.model.enums.InviteOption;
import com.splitspendings.groupexpensesbackend.model.enums.Language;
import com.splitspendings.groupexpensesbackend.model.enums.NotificationCategory;
import com.splitspendings.groupexpensesbackend.model.enums.NotificationOption;
import com.splitspendings.groupexpensesbackend.model.enums.Theme;
import com.splitspendings.groupexpensesbackend.repository.AppUserSettingsRepository;
import com.splitspendings.groupexpensesbackend.repository.GroupInviteRepository;
import com.splitspendings.groupexpensesbackend.repository.GroupMembershipRepository;
import com.splitspendings.groupexpensesbackend.repository.GroupMembershipSettingsRepository;
import com.splitspendings.groupexpensesbackend.repository.GroupRepository;
import com.splitspendings.groupexpensesbackend.repository.ItemCategoryRepository;
import com.splitspendings.groupexpensesbackend.repository.ItemRepository;
import com.splitspendings.groupexpensesbackend.repository.PayoffRepository;
import com.splitspendings.groupexpensesbackend.repository.ShareRepository;
import com.splitspendings.groupexpensesbackend.repository.SpendingCommentRepository;
import com.splitspendings.groupexpensesbackend.repository.SpendingRepository;
import com.splitspendings.groupexpensesbackend.service.AppUserBalanceService;
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
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@SuppressWarnings("all")
public class DatabaseLoader implements CommandLineRunner {

    private static final String ID = "id";
    private static final String USERS_PATH = "test-users/test_user_IDs.txt";
    private static final String ADMINS_PATH = "test-users/test_admin_IDs.txt";

    private final AppUserFactory appUserFactory;
    private final GroupFactory groupFactory;

    private final AppUserBalanceService appUserBalanceService;

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
    private final PayoffRepository payoffRepository;

    private AppUser appUser1;
    private AppUser appUser2;
    private AppUser appUser3;
    private AppUser appUser4;
    private AppUser appUser5;
    private AppUser adminAppUser1;

    private AppUserSettings appUserSettings1;
    private AppUserSettings appUserSettings2;
    private AppUserSettings appUserSettings3;
    private AppUserSettings appUserSettings4;
    private AppUserSettings appUserSettings5;

    private AppUserSettings adminAppUserSettings1;

    private Group group1;
    private Group group2;
    private Group group3;
    private Group group4;

    private GroupMembership groupMembership1_1;
    private GroupMembership groupMembership2_2;
    private GroupMembership groupMembership3_3;
    private GroupMembership groupMembership3_1;
    private GroupMembership groupMembership3_2;
    private GroupMembership groupMembership1_3;
    private GroupMembership groupMembership1_2;
    private GroupMembership groupMembership4_1;
    private GroupMembership groupMembership4_2;
    private GroupMembership groupMembership4_3;
    private GroupMembership groupMembership4_4;
    private GroupMembership groupMembership4_5;

    private GroupMembershipSettings groupMembershipSettings1_1;
    private GroupMembershipSettings groupMembershipSettings2_2;
    private GroupMembershipSettings groupMembershipSettings3_3;
    private GroupMembershipSettings groupMembershipSettings3_1;
    private GroupMembershipSettings groupMembershipSettings3_2;
    private GroupMembershipSettings groupMembershipSettings1_3;
    private GroupMembershipSettings groupMembershipSettings1_2;
    private GroupMembershipSettings groupMembershipSettings4_1;
    private GroupMembershipSettings groupMembershipSettings4_2;
    private GroupMembershipSettings groupMembershipSettings4_3;
    private GroupMembershipSettings groupMembershipSettings4_4;
    private GroupMembershipSettings groupMembershipSettings4_5;

    private Spending spending1_1;
    private Spending spending4_4_1;
    private Spending spending4_4_2;
    private Spending spending4_4_3;

    @SuppressWarnings("all")
    private SpendingComment spendingComment1;
    @SuppressWarnings("all")
    private SpendingComment spendingComment2;
    @SuppressWarnings("all")
    private SpendingComment spendingComment3;

    private ItemCategory itemCategory1;
    @SuppressWarnings("all")
    private ItemCategory itemCategory2;

    private Item item1_1_3;
    private Item item1_1_2;
    private Item item4_1_4;
    private Item item4_1_5;
    private Item item4_2_1;
    private Item item4_2_3;
    private Item item4_3_1;
    private Item item4_3_4;

    @SuppressWarnings("all")

    private Map<String, UUID> loadAppUserIds(String path) {
        Map<String, UUID> idMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
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
        setUpPayoffs();
        recalculate();
    }

    private void recalculate(){
        appUserBalanceService.recalculateAppUserBalanceByGroup(group1);
        appUserBalanceService.recalculateAppUserBalanceByGroup(group2);
        appUserBalanceService.recalculateAppUserBalanceByGroup(group3);
        appUserBalanceService.recalculateAppUserBalanceByGroup(group4);
    }

    private void setUpPayoffs() {
        Payoff payoff4_5_1 = new Payoff();
        payoff4_5_1.setTitle("Payoff4_5_1");
        payoff4_5_1.setCurrency(Currency.PLN);
        payoff4_5_1.setAddedByAppUser(appUser1);
        payoff4_5_1.setGroup(group4);
        payoff4_5_1.setPaidForAppUser(appUser5);
        payoff4_5_1.setPaidToAppUser(appUser1);
        payoff4_5_1.setAmount(new BigDecimal(10));

        Payoff payoff4_5_2 = new Payoff();
        payoff4_5_2.setTitle("Payoff4_5_2");
        payoff4_5_2.setCurrency(Currency.PLN);
        payoff4_5_2.setAddedByAppUser(appUser1);
        payoff4_5_2.setGroup(group4);
        payoff4_5_2.setPaidForAppUser(appUser5);
        payoff4_5_2.setPaidToAppUser(appUser2);
        payoff4_5_2.setAmount(new BigDecimal(30));

        Payoff payoff4_5_3 = new Payoff();
        payoff4_5_3.setTitle("Payoff4_5_3");
        payoff4_5_3.setCurrency(Currency.PLN);
        payoff4_5_3.setAddedByAppUser(appUser1);
        payoff4_5_3.setGroup(group4);
        payoff4_5_3.setPaidForAppUser(appUser5);
        payoff4_5_3.setPaidToAppUser(appUser3);
        payoff4_5_3.setAmount(new BigDecimal(10));

        Payoff payoff4_5_4 = new Payoff();
        payoff4_5_4.setTitle("Payoff4_5_4");
        payoff4_5_4.setCurrency(Currency.PLN);
        payoff4_5_4.setAddedByAppUser(appUser1);
        payoff4_5_4.setGroup(group4);
        payoff4_5_4.setPaidForAppUser(appUser5);
        payoff4_5_4.setPaidToAppUser(appUser4);
        payoff4_5_4.setAmount(new BigDecimal(10));

        payoffRepository.save(payoff4_5_1);
        payoffRepository.save(payoff4_5_2);
        payoffRepository.save(payoff4_5_3);
        payoffRepository.save(payoff4_5_4);
    }

    private void setUpShares() {
        Share share1_1_3 = new Share();
        share1_1_3.setAmount(new BigDecimal("20.02"));
        share1_1_3.setItem(item1_1_3);
        share1_1_3.setCurrency(Currency.EUR);
        share1_1_3.setPaidByGroupMembership(groupMembership1_1);
        share1_1_3.setPaidForGroupMembership(groupMembership1_3);

        Share share1_1_2 = new Share();
        share1_1_2.setAmount(new BigDecimal("30.03"));
        share1_1_2.setItem(item1_1_2);
        share1_1_2.setCurrency(Currency.EUR);
        share1_1_2.setPaidByGroupMembership(groupMembership1_1);
        share1_1_2.setPaidForGroupMembership(groupMembership1_2);

        Share share4_1_4 = new Share();
        share4_1_4.setAmount(new BigDecimal(10));
        share4_1_4.setItem(item4_1_4);
        share4_1_4.setCurrency(Currency.EUR);
        share4_1_4.setPaidByGroupMembership(groupMembership4_1);
        share4_1_4.setPaidForGroupMembership(groupMembership4_4);

        Share share4_1_5 = new Share();
        share4_1_5.setAmount(new BigDecimal(5));
        share4_1_5.setItem(item4_1_5);
        share4_1_5.setCurrency(Currency.EUR);
        share4_1_5.setPaidByGroupMembership(groupMembership4_1);
        share4_1_5.setPaidForGroupMembership(groupMembership4_5);

        Share share4_2_1 = new Share();
        share4_2_1.setAmount(new BigDecimal(10));
        share4_2_1.setItem(item4_2_1);
        share4_2_1.setCurrency(Currency.EUR);
        share4_2_1.setPaidByGroupMembership(groupMembership4_2);
        share4_2_1.setPaidForGroupMembership(groupMembership4_1);

        Share share4_2_3 = new Share();
        share4_2_3.setAmount(new BigDecimal(20));
        share4_2_3.setItem(item4_2_3);
        share4_2_3.setCurrency(Currency.EUR);
        share4_2_3.setPaidByGroupMembership(groupMembership4_2);
        share4_2_3.setPaidForGroupMembership(groupMembership4_3);

        Share share4_3_1 = new Share();
        share4_3_1.setAmount(new BigDecimal(10));
        share4_3_1.setItem(item4_3_1);
        share4_3_1.setCurrency(Currency.EUR);
        share4_3_1.setPaidByGroupMembership(groupMembership4_3);
        share4_3_1.setPaidForGroupMembership(groupMembership4_1);

        Share share4_3_4 = new Share();
        share4_3_4.setAmount(new BigDecimal(20));
        share4_3_4.setItem(item4_3_4);
        share4_3_4.setCurrency(Currency.EUR);
        share4_3_4.setPaidByGroupMembership(groupMembership4_3);
        share4_3_4.setPaidForGroupMembership(groupMembership4_4);

        shareRepository.save(share1_1_3);
        shareRepository.save(share1_1_2);
        shareRepository.save(share4_1_4);
        shareRepository.save(share4_1_5);
        shareRepository.save(share4_2_1);
        shareRepository.save(share4_2_3);
        shareRepository.save(share4_3_1);
        shareRepository.save(share4_3_4);
    }

    private void setUpItems() {
        item1_1_3 = new Item();
        item1_1_3.setItemCategory(itemCategory1);
        item1_1_3.setSpending(spending1_1);
        item1_1_3.setTitle("Dummy item 1");
        item1_1_3.setPrice(new BigDecimal("30.50"));

        item1_1_2 = new Item();
        item1_1_2.setItemCategory(itemCategory1);
        item1_1_2.setSpending(spending1_1);
        item1_1_2.setTitle("Dummy item 1");
        item1_1_2.setPrice(new BigDecimal("30.50"));

        item4_1_4 = new Item();
        item4_1_4.setItemCategory(itemCategory1);
        item4_1_4.setSpending(spending4_4_1);
        item4_1_4.setTitle("Dummy item 1");
        item4_1_4.setPrice(new BigDecimal("30.50"));

        item4_1_5 = new Item();
        item4_1_5.setItemCategory(itemCategory1);
        item4_1_5.setSpending(spending4_4_1);
        item4_1_5.setTitle("Dummy item 1");
        item4_1_5.setPrice(new BigDecimal("30.50"));

        item4_2_1 = new Item();
        item4_2_1.setItemCategory(itemCategory1);
        item4_2_1.setSpending(spending4_4_1);
        item4_2_1.setTitle("Dummy item 1");
        item4_2_1.setPrice(new BigDecimal("30.50"));

        item4_2_3 = new Item();
        item4_2_3.setItemCategory(itemCategory1);
        item4_2_3.setSpending(spending4_4_2);
        item4_2_3.setTitle("Dummy item 1");
        item4_2_3.setPrice(new BigDecimal("30.50"));

        item4_3_1 = new Item();
        item4_3_1.setItemCategory(itemCategory1);
        item4_3_1.setSpending(spending4_4_2);
        item4_3_1.setTitle("Dummy item 1");
        item4_3_1.setPrice(new BigDecimal("30.50"));

        item4_3_4 = new Item();
        item4_3_4.setItemCategory(itemCategory1);
        item4_3_4.setSpending(spending4_4_3);
        item4_3_4.setTitle("Dummy item 1");
        item4_3_4.setPrice(new BigDecimal("30.50"));

        itemRepository.save(item1_1_3);
        itemRepository.save(item1_1_2);
        itemRepository.save(item4_1_4);
        itemRepository.save(item4_1_5);
        itemRepository.save(item4_2_1);
        itemRepository.save(item4_2_3);
        itemRepository.save(item4_3_1);
        itemRepository.save(item4_3_4);
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
        spendingComment1.setSpending(spending1_1);
        spendingComment1.setMessage("Dummy comment 1");
        spendingComment1.setAddedByAppUser(appUser1);
        spendingComment1.setTimeAdded(ZonedDateTime.of(LocalDateTime.of(2020, 8, 27, 12, 34, 56), ZoneId.of("+02:00")));

        spendingComment2 = new SpendingComment();
        spendingComment2.setSpending(spending1_1);
        spendingComment2.setMessage("Dummy comment 2");
        spendingComment2.setAddedByAppUser(appUser1);
        spendingComment2.setTimeAdded(ZonedDateTime.of(LocalDateTime.of(2021, 9, 24, 11, 30, 34), ZoneId.of("-02:00")));

        spendingComment3 = new SpendingComment();
        spendingComment3.setSpending(spending4_4_1);
        spendingComment3.setMessage("Dummy comment 3");
        spendingComment3.setAddedByAppUser(appUser2);
        spendingComment3.setTimeAdded(ZonedDateTime.of(LocalDateTime.of(2020, 9, 24, 11, 30, 34), ZoneId.of("-02:00")));

        spendingCommentRepository.save(spendingComment1);
        spendingCommentRepository.save(spendingComment2);
        spendingCommentRepository.save(spendingComment3);
    }

    private void setUpSpendings() {
        spending1_1 = new Spending();
        spending1_1.setAddedByGroupMembership(groupMembership1_1);
        spending1_1.setCurrency(Currency.EUR);
        spending1_1.setTotalAmount(new BigDecimal("123.50"));
        spending1_1.setTitle("Dummy spending1");

        spending4_4_1 = new Spending();
        spending4_4_1.setAddedByGroupMembership(groupMembership4_2);
        spending4_4_1.setCurrency(Currency.PLN);
        spending4_4_1.setTotalAmount(new BigDecimal("234.56"));
        spending4_4_1.setTitle("Dummy spending2");

        spending4_4_2 = new Spending();
        spending4_4_2.setAddedByGroupMembership(groupMembership4_4);
        spending4_4_2.setCurrency(Currency.PLN);
        spending4_4_2.setTotalAmount(new BigDecimal("234.56"));
        spending4_4_2.setTitle("Dummy spending2");

        spending4_4_3 = new Spending();
        spending4_4_3.setAddedByGroupMembership(groupMembership4_3);
        spending4_4_3.setCurrency(Currency.PLN);
        spending4_4_3.setTotalAmount(new BigDecimal("234.56"));
        spending4_4_3.setTitle("Dummy spending2");

        spendingRepository.save(spending1_1);
        spendingRepository.save(spending4_4_1);
        spendingRepository.save(spending4_4_2);
        spendingRepository.save(spending4_4_3);
    }

    private void setUpGroupInvites() {
        GroupInvite groupInvite1 = new GroupInvite();
        groupInvite1.setMessage("hello");
        groupInvite1.setInvitedAppUser(appUser2);
        groupInvite1.setInvitedByGroupMembership(groupMembership1_1);

        GroupInvite groupInvite2 = new GroupInvite();
        groupInvite2.setInvitedAppUser(appUser3);
        groupInvite2.setInvitedByGroupMembership(groupMembership2_2);

        GroupInvite groupInvite3 = new GroupInvite();
        groupInvite3.setInvitedAppUser(appUser1);
        groupInvite3.setInvitedByGroupMembership(groupMembership3_3);

        GroupInvite groupInvite4 = new GroupInvite();
        groupInvite4.setInvitedAppUser(appUser2);
        groupInvite4.setInvitedByGroupMembership(groupMembership3_3);

        GroupInvite groupInvite5 = new GroupInvite();
        groupInvite5.setInvitedAppUser(appUser2);
        groupInvite5.setInvitedByGroupMembership(groupMembership1_3);

        groupInviteRepository.save(groupInvite1);
        groupInviteRepository.save(groupInvite2);
        groupInviteRepository.save(groupInvite3);
        groupInviteRepository.save(groupInvite4);
        groupInviteRepository.save(groupInvite5);
    }

    @SuppressWarnings("all")
    private void setUpGroupMembershipSettings() {
        groupMembershipSettings1_1 = new GroupMembershipSettings();
        groupMembershipSettings1_1.setGroupMembership(groupMembership1_1);
        groupMembershipSettings1_1.setGroupTheme(GroupTheme.DEFAULT);
        groupMembershipSettings1_1.setNotificationOption(NotificationOption.SELECTED);
        groupMembershipSettings1_1.setNotificationCategories(Set.of(NotificationCategory.NEW_SPENDING));

        groupMembershipSettings2_2 = new GroupMembershipSettings();
        groupMembershipSettings2_2.setGroupMembership(groupMembership2_2);
        groupMembershipSettings2_2.setGroupTheme(GroupTheme.DEFAULT);
        groupMembershipSettings2_2.setNotificationOption(NotificationOption.SELECTED);
        groupMembershipSettings2_2.setNotificationCategories(Set.of(NotificationCategory.NEW_SPENDING));

        groupMembershipSettings3_3 = new GroupMembershipSettings();
        groupMembershipSettings3_3.setGroupMembership(groupMembership3_3);
        groupMembershipSettings3_3.setGroupTheme(GroupTheme.DEFAULT);
        groupMembershipSettings3_3.setNotificationOption(NotificationOption.ALL);

        groupMembershipSettings3_1 = new GroupMembershipSettings();
        groupMembershipSettings3_1.setGroupMembership(groupMembership3_1);
        groupMembershipSettings3_1.setGroupTheme(GroupTheme.DEFAULT);
        groupMembershipSettings3_1.setNotificationOption(NotificationOption.ALL);

        groupMembershipSettings3_2 = new GroupMembershipSettings();
        groupMembershipSettings3_2.setGroupMembership(groupMembership3_2);
        groupMembershipSettings3_2.setGroupTheme(GroupTheme.DEFAULT);
        groupMembershipSettings3_2.setNotificationOption(NotificationOption.ALL);

        groupMembershipSettings1_3 = new GroupMembershipSettings();
        groupMembershipSettings1_3.setGroupMembership(groupMembership1_3);
        groupMembershipSettings1_3.setGroupTheme(GroupTheme.DEFAULT);
        groupMembershipSettings1_3.setNotificationOption(NotificationOption.ALL);

        groupMembershipSettings1_2 = new GroupMembershipSettings();
        groupMembershipSettings1_2.setGroupMembership(groupMembership1_2);
        groupMembershipSettings1_2.setGroupTheme(GroupTheme.DEFAULT);
        groupMembershipSettings1_2.setNotificationOption(NotificationOption.ALL);

        //group 4
        groupMembershipSettings4_1 = new GroupMembershipSettings();
        groupMembershipSettings4_1.setGroupMembership(groupMembership4_1);
        groupMembershipSettings4_1.setGroupTheme(GroupTheme.DEFAULT);
        groupMembershipSettings4_1.setNotificationOption(NotificationOption.ALL);

        groupMembershipSettings4_2 = new GroupMembershipSettings();
        groupMembershipSettings4_2.setGroupMembership(groupMembership4_2);
        groupMembershipSettings4_2.setGroupTheme(GroupTheme.DEFAULT);
        groupMembershipSettings4_2.setNotificationOption(NotificationOption.ALL);

        groupMembershipSettings4_3 = new GroupMembershipSettings();
        groupMembershipSettings4_3.setGroupMembership(groupMembership4_3);
        groupMembershipSettings4_3.setGroupTheme(GroupTheme.DEFAULT);
        groupMembershipSettings4_3.setNotificationOption(NotificationOption.ALL);

        groupMembershipSettings4_4 = new GroupMembershipSettings();
        groupMembershipSettings4_4.setGroupMembership(groupMembership4_4);
        groupMembershipSettings4_4.setGroupTheme(GroupTheme.DEFAULT);
        groupMembershipSettings4_4.setNotificationOption(NotificationOption.ALL);

        groupMembershipSettings4_5 = new GroupMembershipSettings();
        groupMembershipSettings4_5.setGroupMembership(groupMembership4_5);
        groupMembershipSettings4_5.setGroupTheme(GroupTheme.DEFAULT);
        groupMembershipSettings4_5.setNotificationOption(NotificationOption.ALL);

        groupMembershipSettingsRepository.save(groupMembershipSettings1_1);
        groupMembershipSettingsRepository.save(groupMembershipSettings2_2);
        groupMembershipSettingsRepository.save(groupMembershipSettings3_3);
        groupMembershipSettingsRepository.save(groupMembershipSettings3_1);
        groupMembershipSettingsRepository.save(groupMembershipSettings3_2);
        groupMembershipSettingsRepository.save(groupMembershipSettings1_3);
        groupMembershipSettingsRepository.save(groupMembershipSettings1_2);
        groupMembershipSettingsRepository.save(groupMembershipSettings4_1);
        groupMembershipSettingsRepository.save(groupMembershipSettings4_2);
        groupMembershipSettingsRepository.save(groupMembershipSettings4_3);
        groupMembershipSettingsRepository.save(groupMembershipSettings4_4);
        groupMembershipSettingsRepository.save(groupMembershipSettings4_5);
    }

    @SuppressWarnings("all")
    private void setUpGroupMembership() {
        groupMembership1_1 = new GroupMembership();
        groupMembership1_1.setGroup(group1);
        groupMembership1_1.setAppUser(appUser1);
        groupMembership1_1.setActive(true);
        groupMembership1_1.setHasAdminRights(true);
        groupMembership1_1.setFirstTimeJoined(groupMembership1_1.getTimeCreated());
        groupMembership1_1.setLastTimeJoined(groupMembership1_1.getFirstTimeJoined());

        groupMembership2_2 = new GroupMembership();
        groupMembership2_2.setGroup(group2);
        groupMembership2_2.setAppUser(appUser2);
        groupMembership2_2.setActive(false);
        groupMembership2_2.setHasAdminRights(true);
        groupMembership2_2.setFirstTimeJoined(groupMembership2_2.getTimeCreated());
        groupMembership2_2.setLastTimeJoined(groupMembership2_2.getFirstTimeJoined());
        groupMembership2_2.setLastTimeLeft(groupMembership2_2.getFirstTimeJoined().plusWeeks(1));

        groupMembership3_3 = new GroupMembership();
        groupMembership3_3.setGroup(group3);
        groupMembership3_3.setAppUser(appUser3);
        groupMembership3_3.setActive(true);
        groupMembership3_3.setHasAdminRights(true);
        groupMembership3_3.setFirstTimeJoined(groupMembership1_1.getTimeCreated());
        groupMembership3_3.setLastTimeJoined(groupMembership1_1.getFirstTimeJoined());

        groupMembership3_1 = new GroupMembership();
        groupMembership3_1.setGroup(group3);
        groupMembership3_1.setAppUser(appUser1);
        groupMembership3_1.setActive(false);
        groupMembership3_1.setHasAdminRights(false);
        groupMembership3_1.setFirstTimeJoined(groupMembership1_1.getTimeCreated());
        groupMembership3_1.setLastTimeJoined(groupMembership1_1.getFirstTimeJoined());

        groupMembership3_2 = new GroupMembership();
        groupMembership3_2.setGroup(group3);
        groupMembership3_2.setAppUser(appUser2);
        groupMembership3_2.setActive(true);
        groupMembership3_2.setHasAdminRights(false);
        groupMembership3_2.setFirstTimeJoined(groupMembership1_1.getTimeCreated());
        groupMembership3_2.setLastTimeJoined(groupMembership1_1.getFirstTimeJoined());

        groupMembership1_3 = new GroupMembership();
        groupMembership1_3.setGroup(group1);
        groupMembership1_3.setAppUser(appUser3);
        groupMembership1_3.setActive(true);
        groupMembership1_3.setHasAdminRights(false);
        groupMembership1_3.setFirstTimeJoined(groupMembership1_1.getTimeCreated());
        groupMembership1_3.setLastTimeJoined(groupMembership1_1.getFirstTimeJoined());

        groupMembership1_2 = new GroupMembership();
        groupMembership1_2.setGroup(group1);
        groupMembership1_2.setAppUser(appUser2);
        groupMembership1_2.setActive(true);
        groupMembership1_2.setHasAdminRights(false);
        groupMembership1_2.setFirstTimeJoined(groupMembership1_1.getTimeCreated());
        groupMembership1_2.setLastTimeJoined(groupMembership1_1.getFirstTimeJoined());

        //for group4
        groupMembership4_1 = new GroupMembership();
        groupMembership4_1.setGroup(group4);
        groupMembership4_1.setAppUser(appUser1);
        groupMembership4_1.setActive(true);
        groupMembership4_1.setHasAdminRights(false);
        groupMembership4_1.setFirstTimeJoined(groupMembership1_1.getTimeCreated());
        groupMembership4_1.setLastTimeJoined(groupMembership1_1.getFirstTimeJoined());

        groupMembership4_2 = new GroupMembership();
        groupMembership4_2.setGroup(group4);
        groupMembership4_2.setAppUser(appUser2);
        groupMembership4_2.setActive(true);
        groupMembership4_2.setHasAdminRights(false);
        groupMembership4_2.setFirstTimeJoined(groupMembership1_1.getTimeCreated());
        groupMembership4_2.setLastTimeJoined(groupMembership1_1.getFirstTimeJoined());

        groupMembership4_3 = new GroupMembership();
        groupMembership4_3.setGroup(group4);
        groupMembership4_3.setAppUser(appUser3);
        groupMembership4_3.setActive(true);
        groupMembership4_3.setHasAdminRights(false);
        groupMembership4_3.setFirstTimeJoined(groupMembership1_1.getTimeCreated());
        groupMembership4_3.setLastTimeJoined(groupMembership1_1.getFirstTimeJoined());

        groupMembership4_4 = new GroupMembership();
        groupMembership4_4.setGroup(group4);
        groupMembership4_4.setAppUser(appUser4);
        groupMembership4_4.setActive(true);
        groupMembership4_4.setHasAdminRights(false);
        groupMembership4_4.setFirstTimeJoined(groupMembership1_1.getTimeCreated());
        groupMembership4_4.setLastTimeJoined(groupMembership1_1.getFirstTimeJoined());

        groupMembership4_5 = new GroupMembership();
        groupMembership4_5.setGroup(group4);
        groupMembership4_5.setAppUser(appUser5);
        groupMembership4_5.setActive(true);
        groupMembership4_5.setHasAdminRights(false);
        groupMembership4_5.setFirstTimeJoined(groupMembership1_1.getTimeCreated());
        groupMembership4_5.setLastTimeJoined(groupMembership1_1.getFirstTimeJoined());

        groupMembershipRepository.save(groupMembership1_1);
        groupMembershipRepository.save(groupMembership2_2);
        groupMembershipRepository.save(groupMembership3_3);
        groupMembershipRepository.save(groupMembership3_1);
        groupMembershipRepository.save(groupMembership3_2);
        groupMembershipRepository.save(groupMembership1_3);
        groupMembershipRepository.save(groupMembership1_2);
        groupMembershipRepository.save(groupMembership4_1);
        groupMembershipRepository.save(groupMembership4_2);
        groupMembershipRepository.save(groupMembership4_3);
        groupMembershipRepository.save(groupMembership4_4);
        groupMembershipRepository.save(groupMembership4_5);
    }

    private void setUpGroups() {
        group1 = groupFactory.generate();
        group1.setOwner(appUser1);
        group1.setPersonal(false);
        group1.setInviteOption(InviteOption.ALL_ACTIVE_MEMBERS);
        group1.setDefaultCurrency(Currency.EUR);

        group2 = groupFactory.generate();
        group2.setOwner(appUser2);
        group2.setPersonal(true);
        group2.setInviteOption(InviteOption.OWNER_ONLY);
        group2.setDefaultCurrency(Currency.PLN);

        group3 = groupFactory.generate();
        group3.setOwner(appUser3);
        group3.setPersonal(false);
        group3.setInviteOption(InviteOption.OWNER_ONLY);
        group3.setDefaultCurrency(Currency.USD);

        group4 = groupFactory.generate();
        group4.setOwner(appUser1);
        group4.setPersonal(false);
        group4.setInviteOption(InviteOption.OWNER_ONLY);
        group4.setDefaultCurrency(Currency.UAH);

        groupRepository.save(group1);
        groupRepository.save(group2);
        groupRepository.save(group3);
        groupRepository.save(group4);
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

        appUserSettings4 = new AppUserSettings();
        appUserSettings4.setAppUser(appUser4);
        appUserSettings4.setDefaultCurrency(Currency.USD);
        appUserSettings4.setLanguage(Language.EN);
        appUserSettings4.setTheme(Theme.LIGHT);
        appUserSettings4.setGroupInviteOption(GroupInviteOption.ANYONE);
        appUserSettings4.setNotificationOption(NotificationOption.NONE);

        appUserSettings5 = new AppUserSettings();
        appUserSettings5.setAppUser(appUser5);
        appUserSettings5.setDefaultCurrency(Currency.USD);
        appUserSettings5.setLanguage(Language.EN);
        appUserSettings5.setTheme(Theme.LIGHT);
        appUserSettings5.setGroupInviteOption(GroupInviteOption.ANYONE);
        appUserSettings5.setNotificationOption(NotificationOption.NONE);


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
        appUserSettingsRepository.save(appUserSettings4);
        appUserSettingsRepository.save(appUserSettings5);
        appUserSettingsRepository.save(adminAppUserSettings1);
    }

    @SuppressWarnings("all")
    private void setUpAppUsers() throws Exception {
        var usersIdMap = loadAppUserIds(USERS_PATH);
        if (usersIdMap.size() < 2) {
            throw new Exception("not enough test user IDs to load data");
        }

        var adminsIdMap = loadAppUserIds(ADMINS_PATH);
        if (adminsIdMap.size() < 1) {
            throw new Exception("not enough test admin IDs to load data");
        }

        appUser1 = appUserFactory.generate();
        appUser2 = appUserFactory.generate();
        appUser3 = appUserFactory.generate();
        appUser4 = appUserFactory.generate();
        appUser5 = appUserFactory.generate();
        adminAppUser1 = appUserFactory.generate();

        appUser1.setId(usersIdMap.get(ID + 1));
        appUser2.setId(usersIdMap.get(ID + 2));
        appUser3.setId(usersIdMap.get(ID + 3));
        appUser4.setId(usersIdMap.get(ID + 4));
        appUser5.setId(usersIdMap.get(ID + 5));
        adminAppUser1.setId(adminsIdMap.get(ID + 1));
    }
}
