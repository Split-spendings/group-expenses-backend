package com.splitspendings.groupexpensesbackend.dataloader.factory;

import com.splitspendings.groupexpensesbackend.model.AppUser;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class AppUserFactory implements Factory<AppUser> {

    public static final String LOGIN_NAME = "login_name";
    public static final String EMAIL = "email";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";

    private int next_serial_number = 1;

    @Override
    public AppUser generate() {
        int serial_number = next_serial_number;
        AppUser appUser = new AppUser();
        appUser.setLoginName(format(LOGIN_NAME, serial_number));
        appUser.setEmail(format(EMAIL, serial_number));
        appUser.setFirstName(format(FIRST_NAME, serial_number));
        appUser.setLastName(format(LAST_NAME, serial_number));
        appUser.setTimeRegistered(ZonedDateTime.now());
        next_serial_number++;
        return appUser;
    }
}
