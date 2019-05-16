package com.workflowengine.workflowengine.repository;

import com.workflowengine.workflowengine.model.StepAssignee;
import com.workflowengine.workflowengine.model.StepAssigneeIdentity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface StepAssigneeRepository extends JpaRepository<StepAssignee, StepAssigneeIdentity> {

    @Transactional
    @Modifying
    @Query("DELETE FROM StepAssignee u WHERE Stepid = ?1")
    void deleteByStepId(Integer Stepid);

    @Query("SELECT u FROM StepAssignee u WHERE Stepid = ?1")
    List<StepAssignee> findByStepId(Integer stepId);

}
