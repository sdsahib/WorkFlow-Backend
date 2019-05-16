package com.workflowengine.workflowengine.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@Table(name = "StepAssignee")
public class StepAssignee {

    @EmbeddedId
    private StepAssigneeIdentity stepAssigneeIdentity;


}
