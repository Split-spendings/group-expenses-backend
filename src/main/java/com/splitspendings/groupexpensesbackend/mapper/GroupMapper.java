package com.splitspendings.groupexpensesbackend.mapper;

import com.splitspendings.groupexpensesbackend.dto.group.GroupActiveMembersDto;
import com.splitspendings.groupexpensesbackend.dto.group.GroupDto;
import com.splitspendings.groupexpensesbackend.dto.group.GroupSpendingsDto;
import com.splitspendings.groupexpensesbackend.dto.group.NewGroupDto;
import com.splitspendings.groupexpensesbackend.dto.group.UpdateGroupDto;
import com.splitspendings.groupexpensesbackend.model.Group;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    GroupDto groupToGroupInfoDto(Group group);

    List<GroupDto> groupListToGroupInfoDtoList(List<Group> groupList);

    Group newGroupDtoToGroup(NewGroupDto newGroupDto);

    Group copyUpdateGroupInfoDtoToGroup(UpdateGroupDto updateGroupDto, @MappingTarget Group group);

    GroupActiveMembersDto groupToGroupActiveMembersDto(Group group);

    GroupSpendingsDto groupToGroupSpendingsDto(Group group);
}
