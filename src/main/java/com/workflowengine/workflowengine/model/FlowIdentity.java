package com.workflowengine.workflowengine.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class FlowIdentity implements Serializable {
    @Column(name = "StepId")
    private Integer stepId;

    @Column(name = "StatusId")
    private Integer statusId;

    @Column(name = "ChildStepId")
    private Integer childStepId;


    public FlowIdentity(){

    }
    public FlowIdentity(Integer stepId, Integer statusId, Integer childStepId){
        this.stepId = stepId;
        this.statusId = statusId;
        this.childStepId = childStepId;
    }
}
