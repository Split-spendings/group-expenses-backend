package com.splitspendings.groupexpensesbackend.controller.admin;

import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserDto;
import com.splitspendings.groupexpensesbackend.service.AppUserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/admin")
@RequiredArgsConstructor
@SecurityRequirement(name = "identity")
@Slf4j
public class AdminController {

    private final AppUserService appUserService;

    @GetMapping("/users")
    public List<AppUserDto> allAppUsers() {
        return appUserService.allAppUsers();
    }
}
