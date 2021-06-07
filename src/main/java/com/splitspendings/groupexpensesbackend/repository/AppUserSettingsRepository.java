package com.splitspendings.groupexpensesbackend.repository;

import com.splitspendings.groupexpensesbackend.model.AppUserSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserSettingsRepository extends JpaRepository<AppUserSettings, Long> {
}
