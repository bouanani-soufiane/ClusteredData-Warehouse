package com.progressoft.FxDealsWarehouse.mapper;

import com.progressoft.FxDealsWarehouse.dto.DealRequestDto;
import com.progressoft.FxDealsWarehouse.dto.DealResponseDto;
import com.progressoft.FxDealsWarehouse.entity.Deal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DealMapper {

    Deal toEntity( DealRequestDto dto);

    DealResponseDto toResponseEntity( Deal deal);
}
