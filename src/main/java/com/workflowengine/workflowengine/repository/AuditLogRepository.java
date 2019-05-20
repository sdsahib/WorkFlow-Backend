package com.workflowengine.workflowengine.repository;

import com.workflowengine.workflowengine.model.AuditLog;
import com.workflowengine.workflowengine.model.Process;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Integer> {

    @Query("SELECT count(u) FROM AuditLog u WHERE u.modifiedOn IN (SELECT MAX(v.modifiedOn) FROM AuditLog v WHERE u.process = v.process AND u.stepId = v.stepId) AND Processid = ?1 AND Statusid = 5")
    Integer countCompletedStepsWhereProcessId(Integer processId);

    List<AuditLog> findByProcess(Process process);




    @Query("SELECT u FROM AuditLog u WHERE u.modifiedOn IN (SELECT MAX(v.modifiedOn) FROM AuditLog v WHERE u.process = v.process AND u.stepId = v.stepId) AND Processid = ?1 ORDER BY Stepid")
    List<AuditLog> findLatestAuditLogByProcessId(Integer processId);

    @Query("SELECT count(u) FROM AuditLog u WHERE u.modifiedOn IN (SELECT MAX(v.modifiedOn) FROM AuditLog v WHERE u.process = v.process AND u.stepId = v.stepId) AND Processid = ?1")
    Long countLatestAuditLogByProcessId(Integer processId);

    @Query("SELECT u FROM AuditLog u WHERE Processid = ?1 and u.status >1 ORDER BY Stepid, Modifiedon")
    List<AuditLog> findAllAuditLogByProcessId(Integer processId);

}
