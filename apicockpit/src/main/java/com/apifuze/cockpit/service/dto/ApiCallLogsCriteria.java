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

    private StringFilter name;

    private StringFilter code;

    private ApiCallLogsStatusFilter status;

    public ApiCallLogsCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getCode() {
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public ApiCallLogsStatusFilter getStatus() {
        return status;
    }

    public void setStatus(ApiCallLogsStatusFilter status) {
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
        final ApiCallLogsCriteria that = (ApiCallLogsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(code, that.code) &&
            Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        code,
        status
        );
    }

    @Override
    public String toString() {
        return "ApiCallLogsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (code != null ? "code=" + code + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
            "}";
    }

}
