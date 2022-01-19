package com.splitspendings.groupexpensesbackend.mapper;

import com.splitspendings.groupexpensesbackend.dto.group.GroupActiveMembersDto;
import com.splitspendings.groupexpensesbackend.dto.group.GroupDto;
import com.splitspendings.groupexpensesbackend.dto.group.GroupSpendingsDto;
import com.splitspendings.groupexpensesbackend.dto.group.NewGroupDto;
import com.splitspendings.groupexpensesbackend.dto.group.UpdateGroupDto;
import com.splitspendings.groupexpensesbackend.model.Group;
import com.splitspendings.groupexpensesbackend.model.GroupMembership;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = {GroupMembershipMapper.class})
public interface GroupMapper {

    GroupDto groupToGroupInfoDto(Group group, boolean isActiveMember);

    default GroupDto groupToGroupInfoDto(Group group){
        return groupToGroupInfoDto(group, true);
    }

    default List<GroupDto> groupMembershipListToGroupInfoDtoList(List<GroupMembership> groupList){
        if ( groupList == null ) {
            return null;
        }

        List<GroupDto> list = new ArrayList<>(groupList.size());
        for ( GroupMembership groupMembership : groupList ) {
            list.add(groupToGroupInfoDto(groupMembership.getGroup(), groupMembership.getActive()));
        }

        return list;
    }

    Group newGroupDtoToGroup(NewGroupDto newGroupDto);

    void copyUpdateGroupInfoDtoToGroup(UpdateGroupDto updateGroupDto, @MappingTarget Group group);

    GroupActiveMembersDto groupToGroupActiveMembersDto(Group group, List<GroupMembership> members);

    GroupSpendingsDto groupToGroupSpendingsDto(Group group);
}
