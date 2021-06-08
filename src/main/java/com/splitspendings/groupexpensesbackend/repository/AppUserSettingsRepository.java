package com.splitspendings.groupexpensesbackend.repository;

import com.splitspendings.groupexpensesbackend.model.AppUserSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AppUserSettingsRepository extends JpaRepository<AppUserSettings, UUID> {
}
