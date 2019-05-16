package com.workflowengine.workflowengine.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessStepFlowDomain {
    private Integer stepFlowId;
    private Integer processStepModelId;
    private Integer processModelId;
    private Integer toProcessStatusId;
    private Integer moveToStepId;
}
