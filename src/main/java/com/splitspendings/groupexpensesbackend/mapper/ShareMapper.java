package com.splitspendings.groupexpensesbackend.mapper;

import com.splitspendings.groupexpensesbackend.dto.share.NewShareDto;
import com.splitspendings.groupexpensesbackend.model.Share;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShareMapper {

    Share newShareDtoToShare(NewShareDto newShareDto);
}
