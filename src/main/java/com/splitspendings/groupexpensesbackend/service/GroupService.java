package com.splitspendings.groupexpensesbackend.service;

import com.splitspendings.groupexpensesbackend.dto.group.GroupInfoDto;
import com.splitspendings.groupexpensesbackend.model.Group;

public interface GroupService {

    Group groupModelById(Long id);

    GroupInfoDto groupInfoById(Long id);
}
