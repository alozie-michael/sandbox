package com.apifuze.cockpit.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ApiConsumerProfile.
 */
@Entity
@Table(name = "api_consumer_profile")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ApiConsumerProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    private String company;

    @Column(name = "active")
    private Boolean active;

    @NotNull
    @Column(name = "date_created", nullable = false)
    private Instant dateCreated;

    @OneToMany(mappedBy = "owner")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ApiProject> projects = new HashSet<>();
    @OneToOne(mappedBy = "apiConsumerProfile")
    @JsonIgnore
    private PlatformUser platformUser;

    @ManyToOne
    @JsonIgnoreProperties("consumerProfiles")
    private ApiPublisherProfile owner;

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

    public ApiConsumerProfile name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isActive() {
        return active;
    }

    public ApiConsumerProfile active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public ApiConsumerProfile dateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Set<ApiProject> getProjects() {
        return projects;
    }

    public ApiConsumerProfile projects(Set<ApiProject> apiProjects) {
        this.projects = apiProjects;
        return this;
    }

    public ApiConsumerProfile addProjects(ApiProject apiProject) {
        this.projects.add(apiProject);
        apiProject.setOwner(this);
        return this;
    }

    public ApiConsumerProfile removeProjects(ApiProject apiProject) {
        this.projects.remove(apiProject);
        apiProject.setOwner(null);
        return this;
    }

    public void setProjects(Set<ApiProject> apiProjects) {
        this.projects = apiProjects;
    }

    public PlatformUser getPlatformUser() {
        return platformUser;
    }

    public ApiConsumerProfile platformUser(PlatformUser platformUser) {
        this.platformUser = platformUser;
        return this;
    }

    public void setPlatformUser(PlatformUser platformUser) {
        this.platformUser = platformUser;
    }

    public ApiPublisherProfile getOwner() {
        return owner;
    }

    public ApiConsumerProfile owner(ApiPublisherProfile apiPublisherProfile) {
        this.owner = apiPublisherProfile;
        return this;
    }

    public void setOwner(ApiPublisherProfile apiPublisherProfile) {
        this.owner = apiPublisherProfile;
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
        ApiConsumerProfile apiConsumerProfile = (ApiConsumerProfile) o;
        if (apiConsumerProfile.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), apiConsumerProfile.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ApiConsumerProfile{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", active='" + isActive() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            "}";
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
