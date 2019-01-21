package com.apifuze.cockpit.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import com.apifuze.cockpit.domain.enumeration.ApiCallLogsStatus;

/**
 * A ApiCallLogs.
 */
@Entity
@Table(name = "api_call_logs")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ApiCallLogs implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApiCallLogsStatus status;

    @NotNull
    @Column(name = "project_name", nullable = false)
    private String projectName;

    @NotNull
    @Column(name = "api_name", nullable = false)
    private String apiName;

    @NotNull
    @Column(name = "jhi_profile", nullable = false)
    private String profile;

    @NotNull
    @Column(name = "request_date", nullable = false)
    private Instant requestDate;

    @NotNull
    @Column(name = "response_date", nullable = false)
    private Instant responseDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApiCallLogsStatus getStatus() {
        return status;
    }

    public ApiCallLogs status(ApiCallLogsStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(ApiCallLogsStatus status) {
        this.status = status;
    }

    public String getProjectName() {
        return projectName;
    }

    public ApiCallLogs projectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getApiName() {
        return apiName;
    }

    public ApiCallLogs apiName(String apiName) {
        this.apiName = apiName;
        return this;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getProfile() {
        return profile;
    }

    public ApiCallLogs profile(String profile) {
        this.profile = profile;
        return this;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Instant getRequestDate() {
        return requestDate;
    }

    public ApiCallLogs requestDate(Instant requestDate) {
        this.requestDate = requestDate;
        return this;
    }

    public void setRequestDate(Instant requestDate) {
        this.requestDate = requestDate;
    }

    public Instant getResponseDate() {
        return responseDate;
    }

    public ApiCallLogs responseDate(Instant responseDate) {
        this.responseDate = responseDate;
        return this;
    }

    public void setResponseDate(Instant responseDate) {
        this.responseDate = responseDate;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ApiCallLogs apiCallLogs = (ApiCallLogs) o;
        if (apiCallLogs.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), apiCallLogs.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ApiCallLogs{" +
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
