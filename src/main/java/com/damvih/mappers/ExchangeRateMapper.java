package com.damvih.mappers;

import com.damvih.dto.ExchangeResponse;
import com.damvih.entities.ExchangeRate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ExchangeRateMapper {

    ExchangeRateMapper INSTANCE = Mappers.getMapper(ExchangeRateMapper.class);

    ExchangeResponse toDto(ExchangeRate entity);

}
