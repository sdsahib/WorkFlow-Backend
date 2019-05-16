package com.workflowengine.workflowengine.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WorkFlowStepDomain {
    private Integer id;
    private Integer workFlowBluePrintId;
    //TODO remove the group id and
    private Integer groupId;
    private String groupName;
    private Integer sequenceNumber;
    private String name;
    private String details;
    private Integer deadline;
    private String period;

}
