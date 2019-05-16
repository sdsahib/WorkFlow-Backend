package com.workflowengine.workflowengine.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProcessStepDomain {
    private Integer processStepId;
    private Integer processId;
    private Integer groupId;
    private Integer workflowBluePrintStepId;
    private String processStepName;
    private String processDetail;
    private String processComments;
    private String groupName;
    private Integer sequenceNumber;
    private String status;
    private LocalDateTime deadline;
    private LocalDateTime updatedOn;
    private String updatedBy;
}
