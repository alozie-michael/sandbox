package com.apifuze.cockpit.service.dto;

import java.io.Serializable;
import java.util.Objects;
import com.apifuze.cockpit.domain.enumeration.ApiCallLogsStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the ApiCallLogs entity. This class is used in ApiCallLogsResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /api-call-logs?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ApiCallLogsCriteria implements Serializable {
    /**
     * Class for filtering ApiCallLogsStatus
     */
    public static class ApiCallLogsStatusFilter extends Filter<ApiCallLogsStatus> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ApiCallLogsStatusFilter status;

    private StringFilter projectName;

    private StringFilter apiName;

    private StringFilter profile;

    private InstantFilter requestDate;

    private InstantFilter responseDate;

    public ApiCallLogsCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ApiCallLogsStatusFilter getStatus() {
        return status;
    }

    public void setStatus(ApiCallLogsStatusFilter status) {
        this.status = status;
    }

    public StringFilter getProjectName() {
        return projectName;
    }

    public void setProjectName(StringFilter projectName) {
        this.projectName = projectName;
    }

    public StringFilter getApiName() {
        return apiName;
    }

    public void setApiName(StringFilter apiName) {
        this.apiName = apiName;
    }

    public StringFilter getProfile() {
        return profile;
    }

    public void setProfile(StringFilter profile) {
        this.profile = profile;
    }

    public InstantFilter getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(InstantFilter requestDate) {
        this.requestDate = requestDate;
    }

    public InstantFilter getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(InstantFilter responseDate) {
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
        final ApiCallLogsCriteria that = (ApiCallLogsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(status, that.status) &&
            Objects.equals(projectName, that.projectName) &&
            Objects.equals(apiName, that.apiName) &&
            Objects.equals(profile, that.profile) &&
            Objects.equals(requestDate, that.requestDate) &&
            Objects.equals(responseDate, that.responseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        status,
        projectName,
        apiName,
        profile,
        requestDate,
        responseDate
        );
    }

    @Override
    public String toString() {
        return "ApiCallLogsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (projectName != null ? "projectName=" + projectName + ", " : "") +
                (apiName != null ? "apiName=" + apiName + ", " : "") +
                (profile != null ? "profile=" + profile + ", " : "") +
                (requestDate != null ? "requestDate=" + requestDate + ", " : "") +
                (responseDate != null ? "responseDate=" + responseDate + ", " : "") +
            "}";
    }

}
