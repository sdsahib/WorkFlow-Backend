package com.workflowengine.workflowengine.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "Template")
public class WFTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TemplateId",unique = true,nullable = false)
    private Integer wfTemplateId;

    @Column(name = "Name")
    private String name;

    @Column(name = "CreatedBy")
    private Integer createdBy;

    @Column(name = "ModifiedBy")
    private Integer modifiedBy;

    @Column(name = "CreatedOn")
    private LocalDateTime createdOn;

    @Column(name = "ModifiedOn")
    private LocalDateTime modifiedOn;

    public WFTemplate(){

    }
    public WFTemplate(Integer id){
        this.wfTemplateId = id;
    }
}
