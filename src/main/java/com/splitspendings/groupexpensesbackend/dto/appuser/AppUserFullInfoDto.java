package com.splitspendings.groupexpensesbackend.dto.appuser;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class AppUserFullInfoDto {

    private UUID id;
    private String loginName;
    private String email;
    private String firstName;
    private String lastName;
    private ZonedDateTime timeRegistered;
}
