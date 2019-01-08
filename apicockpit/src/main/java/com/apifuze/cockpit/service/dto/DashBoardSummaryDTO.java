package com.apifuze.cockpit.service.dto;

import org.springframework.boot.actuate.audit.AuditEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A DTO for the DashBoard entity.
 */
public class DashBoardSummaryDTO implements Serializable {


    DashBoardSummaryData subscribedServices;

    DashBoardSummaryData totalApiCalls;

    DashBoardSummaryData totalApiErrorCalls;

    List<AuditEvent> userActivityDTOList=new ArrayList<>();

    public List<AuditEvent> getUserActivityDTOList() {
        return userActivityDTOList;
    }

    public void setUserActivityDTOList(List<AuditEvent> userActivityDTOList) {
        this.userActivityDTOList = userActivityDTOList;
    }



    public DashBoardSummaryData getSubscribedServices() {
        return subscribedServices;
    }

    public void setSubscribedServices(DashBoardSummaryData subscribedServices) {
        this.subscribedServices = subscribedServices;
    }

    public DashBoardSummaryData getTotalApiCalls() {
        return totalApiCalls;
    }

    public void setTotalApiCalls(DashBoardSummaryData totalApiCalls) {
        this.totalApiCalls = totalApiCalls;
    }

    public DashBoardSummaryData getTotalApiErrorCalls() {
        return totalApiErrorCalls;
    }

    public void setTotalApiErrorCalls(DashBoardSummaryData totalApiErrorCalls) {
        this.totalApiErrorCalls = totalApiErrorCalls;
    }
}
