package com.apifuze.cockpit.service.dto;

import java.time.Instant;
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
    private ApiCallLogsStatus status;

    @NotNull
    private String projectName;

    @NotNull
    private String apiName;

    @NotNull
    private String profile;

    @NotNull
    private Instant requestDate;

    @NotNull
    private Instant responseDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApiCallLogsStatus getStatus() {
        return status;
    }

    public void setStatus(ApiCallLogsStatus status) {
        this.status = status;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Instant getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Instant requestDate) {
        this.requestDate = requestDate;
    }

    public Instant getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(Instant responseDate) {
        this.responseDate = responseDate;
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
            ", status='" + getStatus() + "'" +
            ", projectName='" + getProjectName() + "'" +
            ", apiName='" + getApiName() + "'" +
            ", profile='" + getProfile() + "'" +
            ", requestDate='" + getRequestDate() + "'" +
            ", responseDate='" + getResponseDate() + "'" +
            "}";
    }
}
