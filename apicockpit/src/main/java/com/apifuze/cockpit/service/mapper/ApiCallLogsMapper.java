package com.apifuze.cockpit.service.mapper;

import com.apifuze.cockpit.domain.*;
import com.apifuze.cockpit.service.dto.ApiCallLogsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ApiCallLogs and its DTO ApiCallLogsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ApiCallLogsMapper extends EntityMapper<ApiCallLogsDTO, ApiCallLogs> {



    default ApiCallLogs fromId(Long id) {
        if (id == null) {
            return null;
        }
        ApiCallLogs apiCallLogs = new ApiCallLogs();
        apiCallLogs.setId(id);
        return apiCallLogs;
    }
}
