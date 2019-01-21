package com.apifuze.cockpit.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.apifuze.cockpit.domain.enumeration.ApiCallLogsStatus;

/**
 * A DTO for the ApiCallLogs entity.
 */
public class ApiCallLogsDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String code;

    @NotNull
    private ApiCallLogsStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ApiCallLogsStatus getStatus() {
        return status;
    }

    public void setStatus(ApiCallLogsStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ApiCallLogsDTO apiCallLogsDTO = (ApiCallLogsDTO) o;
        if (apiCallLogsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), apiCallLogsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ApiCallLogsDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
