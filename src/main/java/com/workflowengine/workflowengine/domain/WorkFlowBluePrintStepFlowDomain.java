package com.workflowengine.workflowengine.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkFlowBluePrintStepFlowDomain {
    private Integer stepId;
    private Integer statusId;
    private Integer childStepId;
}
