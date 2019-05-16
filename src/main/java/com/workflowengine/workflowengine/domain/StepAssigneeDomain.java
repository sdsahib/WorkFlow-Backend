package com.workflowengine.workflowengine.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Getter
@Setter
@ToString
public class StepAssigneeDomain {
    private Integer stepId;
    private List<RoleDomain> groupModelList;
}
