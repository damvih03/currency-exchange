package com.damvih.mappers;

import com.damvih.dto.CurrencyRequest;
import com.damvih.entities.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CurrencyMapper {

    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);

    Currency toEntity(CurrencyRequest currencyRequest);

}
