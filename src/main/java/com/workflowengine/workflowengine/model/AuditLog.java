package com.workflowengine.workflowengine.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "AuditLog")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AuditId", unique = true,updatable = false, nullable = false)
    private Integer auditId;

    @ManyToOne
    @JoinColumn(name = "ProcessId")
    private Process process;

    @Column(name = "StepId")
    private Integer stepId;

    @ManyToOne
    @JoinColumn(name = "StatusId")
    private Status status;

    @Column(name = "CreatedBy")
    private Integer createdBy;

    @Column(name = "CreatedOn")
    private LocalDateTime createdOn;

    @Column(name = "Comments")
    private String comments;

    @Column(name = "ModifiedBy")
    private Integer modifiedBy;

    @Column(name = "ModifiedOn")
    private LocalDateTime modifiedOn;

    @Column(name = "Deadline")
    private LocalDateTime deadline;

    @Override
    public boolean equals(Object obj){
        if(this == obj)
            return true;
        if(auditId == null || obj == null || getClass() != obj.getClass() )
            return false;
        AuditLog that = (AuditLog) obj;
        return auditId.equals(that.auditId);
    }
    @Override
    public int hashCode() {
        return auditId == null ? 0 : auditId.hashCode();
    }
}
