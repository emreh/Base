package com.supplychain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface GenericMapper<DTO, ENTITY> {

    ENTITY toEntity(DTO dto);

    void updateEntityFromDto(DTO dto, @MappingTarget ENTITY entity);
}