package com.apifuze.cockpit.web.rest;

import com.apifuze.cockpit.service.ApiProjectService;
import com.apifuze.cockpit.service.AuditEventService;
import com.apifuze.cockpit.service.dto.ApiProjectDTO;
import com.apifuze.cockpit.service.dto.DashBoardSummaryDTO;
import com.apifuze.cockpit.service.dto.DashBoardSummaryData;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for getting dashboard data.
 */
@RestController
@RequestMapping("/api")
public class DashBoardResource {

    private final Logger log = LoggerFactory.getLogger(DashBoardResource.class);


    private final ApiProjectService apiProjectService;

    private final AuditEventService auditEventService;



    public DashBoardResource(ApiProjectService apiProjectService,AuditEventService auditEventService) {
        this.apiProjectService = apiProjectService;
        this.auditEventService=auditEventService;
    }


    /**
     * GET  /dashboard : get dash board summary data
     * @return the ResponseEntity with status 200 (OK) and the list of DashBoardSummaryDTO in body
     */
    @GetMapping("/dashboard")
    @Timed
    public ResponseEntity<DashBoardSummaryDTO> getDashDetails() {
        DashBoardSummaryDTO summary=new DashBoardSummaryDTO();

        log.debug("REST request to get a page of ApiProjects");
        List<ApiProjectDTO> services=apiProjectService.findAll();
        DashBoardSummaryData data=new DashBoardSummaryData();
        data.setName("services");
        data.setDescription("Subscribed Services");
        data.setCount(0);
        if(services!=null){
            data.setCount(services.size());
        }
        summary.setSubscribedServices(data);


        data=new DashBoardSummaryData();
        data.setName("serviceError");
        data.setDescription("Total Error Calls");
        data.setCount(0);
        summary.setTotalApiCalls(data);

        data=new DashBoardSummaryData();
        data.setName("serviceCalls");
        data.setDescription("Total Api Calls");
        data.setCount(0);
        summary.setTotalApiErrorCalls(data);
        Page<AuditEvent> userEvent = auditEventService.findLastXUserEvent(10);
        summary.setUserActivityDTOList(userEvent.getContent());
        return ResponseEntity.ok().body(summary);
    }

}