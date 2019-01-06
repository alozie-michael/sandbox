package com.apifuze.cockpit.repository;

import com.apifuze.cockpit.domain.ApiProject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the ApiProject entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApiProjectRepository extends JpaRepository<ApiProject, Long> {

    @Query(value = "select distinct api_project from ApiProject api_project left join fetch api_project.apis",
        countQuery = "select count(distinct api_project) from ApiProject api_project")
    Page<ApiProject> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct api_project from ApiProject api_project left join fetch api_project.apis")
    List<ApiProject> findAllWithEagerRelationships();

    @Query("select api_project from ApiProject api_project left join fetch api_project.apis where api_project.id =:id")
    Optional<ApiProject> findOneWithEagerRelationships(@Param("id") Long id);

    Page<ApiProject>  findAllByOwnerId(Long ownerId, Pageable pageable);

    List<ApiProject>  findAllByOwnerId(Long ownerId);
}
