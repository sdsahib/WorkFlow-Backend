package com.workflowengine.workflowengine.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessDomain {
    private Integer processId;
    private Integer BluePrintId;
    private Integer userId;
    private String processName;
    private Integer processSteps;
    private String userName;
    private Integer stepsCompleted;
    private String lastStatus;
}
