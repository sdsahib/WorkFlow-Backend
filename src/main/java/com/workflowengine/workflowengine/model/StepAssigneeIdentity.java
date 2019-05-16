package com.workflowengine.workflowengine.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class StepAssigneeIdentity implements Serializable {
    @Column(name = "StepId")
    private Integer stepid;

    @Column(name = "RoleId")
    private Integer groupId;

    public StepAssigneeIdentity(Integer stepid, Integer groupId){
        this.stepid = stepid;
        this.groupId = groupId;
    }
    public StepAssigneeIdentity(){

    }

}
