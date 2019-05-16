package com.workflowengine.workflowengine.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "Flow")
public class Flow {
    @EmbeddedId
    private FlowIdentity flowIdentity;

    public Flow(){

    }
    public Flow(FlowIdentity flowIdentity){
        this.flowIdentity = flowIdentity;
    }
}
