package com.workflowengine.workflowengine.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "Step")
public class Step {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StepId",unique = true,nullable = false)
    private Integer stepId;

    @ManyToOne
    @JoinColumn(name = "TemplateId")
    private WFTemplate wfTemplate;

    @Column(name = "Name")
    private String name;

    @Column(name = "Details")
    private String details;

    @Column(name = "DeadlinePeriod")
    private Integer deadlinePeriod;

    @Column(name = "DeadlineUnit")
    private String deadlineUnit;

    @Column(name = "Active")
    private Boolean active;
    public Step(){
    }
    public Step(Integer stepId){
        this.stepId = stepId;
    }


}
