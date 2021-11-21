package com.splitspendings.groupexpensesbackend.mapper;

import com.splitspendings.groupexpensesbackend.dto.payoff.PayoffDto;
import com.splitspendings.groupexpensesbackend.model.Payoff;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PayoffMapper {
    PayoffDto payoffToPayoffDto(Payoff payoff);
}