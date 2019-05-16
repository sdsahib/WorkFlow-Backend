package com.workflowengine.workflowengine.repository;


import com.workflowengine.workflowengine.model.Step;
import com.workflowengine.workflowengine.model.WFTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StepRepository extends JpaRepository<Step, Integer> {
    List<Step> findByWfTemplateAndActive(WFTemplate wfTemplate, Boolean active);

}
