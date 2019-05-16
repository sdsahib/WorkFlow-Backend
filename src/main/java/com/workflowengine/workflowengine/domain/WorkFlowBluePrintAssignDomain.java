package com.workflowengine.workflowengine.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkFlowBluePrintAssignDomain {
    private Integer id;
    private Integer workFlowBluePrintId;
    private Integer groupId;
    private Integer sequenceNumber;
    private Integer workFlowStepId;
    private String groupName;
}
