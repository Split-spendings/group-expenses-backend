package com.splitspendings.groupexpensesbackend.mapper;

import com.splitspendings.groupexpensesbackend.dto.group.GroupInfoDto;
import com.splitspendings.groupexpensesbackend.dto.group.NewGroupDto;
import com.splitspendings.groupexpensesbackend.dto.group.UpdateGroupInfoDto;
import com.splitspendings.groupexpensesbackend.model.Group;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    GroupInfoDto groupToGroupInfoDto(Group group);

    List<GroupInfoDto> groupListToGroupInfoDtoList(List<Group> groupList);

    Group newGroupDtoToGroup(NewGroupDto newGroupDto);

    Group copyUpdateGroupInfoDtoToGroup(UpdateGroupInfoDto updateGroupInfoDto, @MappingTarget Group group);
}
