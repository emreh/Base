package com.supplychain.base.mapping;

import org.mapstruct.MappingTarget;

public interface GenericMapper<DTO, ENTITY> {

    ENTITY toEntity(DTO dto);

    void updateEntityFromDto(DTO dto, @MappingTarget ENTITY entity);
}
