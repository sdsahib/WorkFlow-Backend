package com.workflowengine.workflowengine.repository;

import com.workflowengine.workflowengine.model.Flow;
import com.workflowengine.workflowengine.model.FlowIdentity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlowRepository extends JpaRepository<Flow, FlowIdentity> {
    @Query("SELECT u FROM Flow u WHERE Stepid = ?1")
    List<Flow> findByStepId(Integer stepId);

//    @Query("SELECT ")
//    List<Flow> findAllByProcessIdOrderByStepId(Integer processId);
}
