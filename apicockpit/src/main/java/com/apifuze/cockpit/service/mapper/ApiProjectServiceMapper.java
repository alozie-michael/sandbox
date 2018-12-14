package com.apifuze.cockpit.service.mapper;

import com.apifuze.cockpit.domain.*;
import com.apifuze.cockpit.service.dto.ApiProjectServiceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ApiProjectService and its DTO ApiProjectServiceDTO.
 */
@Mapper(componentModel = "spring", uses = {ApiServiceConfigMapper.class})
public interface ApiProjectServiceMapper extends EntityMapper<ApiProjectServiceDTO, ApiProjectService> {

    @Mapping(source = "serviceConfig.id", target = "serviceConfigId")
    @Mapping(source = "serviceConfig.name", target = "serviceConfigName")
    @Mapping(source = "serviceConfig.serviceGroup.name", target = "groupName")
    ApiProjectServiceDTO toDto(ApiProjectService apiProjectService);

    @Mapping(source = "serviceConfigId", target = "serviceConfig")
    ApiProjectService toEntity(ApiProjectServiceDTO apiProjectServiceDTO);

    default ApiProjectService fromId(Long id) {
        if (id == null) {
            return null;
        }
        ApiProjectService apiProjectService = new ApiProjectService();
        apiProjectService.setId(id);
        return apiProjectService;
    }
}
