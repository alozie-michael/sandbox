package com.apifuze.cockpit.repository;

import com.apifuze.cockpit.domain.ApiCallLogs;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ApiCallLogs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApiCallLogsRepository extends JpaRepository<ApiCallLogs, Long>, JpaSpecificationExecutor<ApiCallLogs> {

}
