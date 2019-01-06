package com.apifuze.cockpit.service.dto;

import java.io.Serializable;

/**
 * A DTO for the DashBoard entity.
 */
public class DashBoardSummaryDTO implements Serializable {


    DashBoardSummaryData subscribedServices;

    DashBoardSummaryData totalApiCalls;

    DashBoardSummaryData totalApiErrorCalls;

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
