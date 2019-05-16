package com.workflowengine.workflowengine.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "Process")
public class Process {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProcessId", unique = true, nullable = false)
    private Integer processId;

    @Column(name = "Name")
    private String name;

    @Column(name = "Comments")
    private String comments;

    @ManyToOne
    @JoinColumn(name = "TemplateId")
    private WFTemplate wfTemplate;

    @Column(name = "CreatedBy")
    private Integer createdBy;

    @Column(name = "ModifiedBy")
    private Integer modifiedBy;

    @Column(name = "CreatedOn")
    private LocalDateTime createdOn;

    @Column(name = "ModifiedOn")
    private LocalDateTime ModifiedOn;

    @ManyToOne
    @JoinColumn(name = "StatusId")
    private Status statusId;

    public Process() {

    }

    public Process(Integer processId) {
        this.processId = processId;
    }

}
