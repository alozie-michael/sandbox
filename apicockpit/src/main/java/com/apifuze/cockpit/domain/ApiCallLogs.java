package com.apifuze.cockpit.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
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
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApiCallLogsStatus status;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public ApiCallLogs name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public ApiCallLogs code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
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
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
