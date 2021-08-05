package com.splitspendings.groupexpensesbackend.repository;

import com.splitspendings.groupexpensesbackend.model.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long> {
}
