package com.workflowengine.workflowengine.repository;

import com.workflowengine.workflowengine.model.Process;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessRepository extends JpaRepository<Process, Integer> {
}
