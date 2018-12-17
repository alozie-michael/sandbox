package com.apifuze.cockpit.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the ApiProjectService entity.
 */
public class ApiProjectServiceDTO implements Serializable {

    private Long id;

    private Boolean active;

    private String name;

    @NotNull
    private Instant dateCreated;

    private Long serviceConfigId;

    private String serviceConfigName;



    private String serviceGroupName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Long getServiceConfigId() {
        return serviceConfigId;
    }

    public void setServiceConfigId(Long apiServiceConfigId) {
        this.serviceConfigId = apiServiceConfigId;
    }

    public String getServiceConfigName() {
        return serviceConfigName;
    }

    public void setServiceConfigName(String apiServiceConfigName) {
        this.serviceConfigName = apiServiceConfigName;
    }

    public String getServiceGroupName() {
        return serviceGroupName;
    }

    public void setServiceGroupName(String serviceGroupName) {
        this.serviceGroupName = serviceGroupName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ApiProjectServiceDTO apiProjectServiceDTO = (ApiProjectServiceDTO) o;
        if (apiProjectServiceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), apiProjectServiceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ApiProjectServiceDTO{" +
            "id=" + getId() +
            ", active='" + isActive() + "'" +
            ", name='" + getName() + "'" +
            ", groupName='" + getServiceGroupName() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", serviceConfig=" + getServiceConfigId() +
            ", serviceConfig='" + getServiceConfigName() + "'" +
            "}";
    }
}
