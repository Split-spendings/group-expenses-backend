package com.splitspendings.groupexpensesbackend.mapper;

import com.splitspendings.groupexpensesbackend.dto.share.NewShareDto;
import com.splitspendings.groupexpensesbackend.dto.share.ShareDto;
import com.splitspendings.groupexpensesbackend.model.Share;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AppUserMapper.class})
public interface ShareMapper {

    Share newShareDtoToShare(NewShareDto newShareDto);

    @Mapping(target = "paidFor", source = "paidForGroupMembership.appUser")
    ShareDto shareToShareDto(Share share);
}
