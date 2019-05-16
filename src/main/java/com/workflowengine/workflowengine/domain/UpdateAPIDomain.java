package com.workflowengine.workflowengine.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAPIDomain {
    private Integer processId;
    private Integer stepId;
    private Integer statusId;
}
