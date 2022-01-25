package com.splitspendings.groupexpensesbackend.mapper;

import com.splitspendings.groupexpensesbackend.dto.payoff.NewPayoffDto;
import com.splitspendings.groupexpensesbackend.dto.payoff.PayoffDto;
import com.splitspendings.groupexpensesbackend.dto.payoff.UpdatePayoffDto;
import com.splitspendings.groupexpensesbackend.model.AppUser;
import com.splitspendings.groupexpensesbackend.model.Group;
import com.splitspendings.groupexpensesbackend.model.Payoff;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Set;

@Mapper(componentModel = "spring", uses = {AppUserMapper.class, GroupMapper.class})
public interface PayoffMapper {
    PayoffDto payoffToPayoffDto(Payoff payoff);

    @Mapping(target = "payoff.id", ignore = true)
    @Mapping(target = "payoff.addedByAppUser", source = "current")
    @Mapping(target = "payoff.paidForAppUser", source = "paidFor")
    @Mapping(target = "payoff.paidToAppUser", source = "paidTo")
    void copyUpdatePayoffDtoToPayoff(UpdatePayoffDto updatePayoffDto, AppUser current, AppUser paidFor,
                                       AppUser paidTo, @MappingTarget Payoff payoff);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timeCreated", ignore = true)
    @Mapping(target = "group", source = "group")
    @Mapping(target = "addedByAppUser", source = "current")
    @Mapping(target = "paidForAppUser", source = "paidFor")
    @Mapping(target = "paidToAppUser", source = "paidTo")
    Payoff newPayoffDtoToPayoff(NewPayoffDto newPayoffDto, Group group, AppUser current, AppUser paidFor, AppUser paidTo);

    Set<PayoffDto> payoffSetToPayoffDtoSet(Set<Payoff> payoffSet);
}