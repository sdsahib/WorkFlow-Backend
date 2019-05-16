package com.workflowengine.workflowengine.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "Status")
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StatusId",unique = true,nullable = false)
    private Integer statusId;

    @Column(name = "Name")
    private String name;

    @Column(name = "Description")
    private String description;

    public Status() {

    }

    public Status(Integer statusId) {
        this.statusId = statusId;
    }
}
