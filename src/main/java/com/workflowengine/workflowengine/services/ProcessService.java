package com.workflowengine.workflowengine.services;


import com.workflowengine.workflowengine.domain.ProcessDomain;
import com.workflowengine.workflowengine.domain.ProcessStepDomain;
import com.workflowengine.workflowengine.domain.UpdateAPIDomain;
import com.workflowengine.workflowengine.model.*;
import com.workflowengine.workflowengine.model.Process;
import com.workflowengine.workflowengine.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
public class ProcessService {

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private WFTemplateRepository wfTemplateRepository;
    @Autowired
    private StepRepository stepRepository;
    @Autowired
    private AuditLogRepository auditLogRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private AuthPermissionService authPermissionService;


    private final Logger logger = LoggerFactory.getLogger(ProcessService.class);

    // TODODone 1: should create the entry in the process table
    // DONETODO 4: Replicate all the steps of the workflow to the process steps table
    // DONETODO 2: Create the ProcessModel Domain and return that in
    // DONETODO 3 Create the Function for saving the process step in db

    public APIResponse createProcess(Integer workFlowId, String processName) {
        APIResponse toReturnApiResponse = new APIResponse(404, "Error");
        try {
            //get the WorkFlowBluePrintModel
            WFTemplate wfTemplate = this.wfTemplateRepository.findById(workFlowId).get();
            LocalDateTime now = LocalDateTime.now();
            //Save the Process Step with the Name
            Process toSave = new Process();
            toSave.setName(processName);
            SysUser u = this.authPermissionService.getCurrentUser();
            toSave.setCreatedBy((int) u.getId());
            toSave.setCreatedOn(now);
            toSave.setModifiedBy((int) u.getId());
            toSave.setModifiedOn(now);
            toSave.setWfTemplate(wfTemplate);
            //setting the status as started

            toSave.setStatusId(this.statusRepository.findById(2).get());

            Process savedProcess = this.processRepository.save(toSave);

            //Fetching all the bluePrintSteps and creating the process Steps

            List<Step> blueprintSteps = this.stepRepository.findByWfTemplateAndActive(wfTemplate, true);

            //creating the Process Step in AuditLog
            this.createProcessStep(blueprintSteps, savedProcess.getProcessId(), (int) u.getId());


            ProcessDomain savedProcessModel = this.convertToProcessDomain(savedProcess, blueprintSteps.size(), 0, (int) u.getId(), u.getFirstName() + ' ' + u.getLastName());

            toReturnApiResponse.setStatus(200);
            toReturnApiResponse.setDescription("Process Created");
            toReturnApiResponse.setData(Collections.singletonList(savedProcessModel));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturnApiResponse;
    }

    public APIResponse createProcessStep(List<Step> listToSave, Integer processID, Integer userId) {
        APIResponse toReturnApiResponse = new APIResponse(404, "Error");
        List<AuditLog> toReturn = new ArrayList<>();
        LocalDateTime currentDate = LocalDateTime.now();
        try {
            for (int i = 0; i < listToSave.size(); i++) {
                AuditLog toSave = this.convertToAuditLogFromStep(listToSave.get(i), processID, currentDate, userId);
                if (i == 0) {
                    toSave.setDeadline(this.getDeadline(listToSave.get(i).getDeadlinePeriod(), listToSave.get(i).getDeadlineUnit()));
                }
                toReturn.add(this.auditLogRepository.save(toSave));
            }
            toReturnApiResponse.setStatus(200);
            toReturnApiResponse.setDescription("Process Created");
            toReturnApiResponse.setData(Collections.singletonList(toReturn));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return toReturnApiResponse;
    }

    public APIResponse getProcessDetails(Integer processId) {
        APIResponse toReturnApiResponse = new APIResponse(404, "Error");
        try {

            Process fetched = this.processRepository.findById(processId).get();
            Integer size = Math.toIntExact(this.auditLogRepository.countLatestAuditLogByProcessId(processId));
            Integer count = this.auditLogRepository.countCompletedStepsWhereProcessId(processId);
            SysUser u = this.authPermissionService.getCurrentUser();
            ProcessDomain toAttach = this.convertToProcessDomain(fetched, size, count, fetched.getCreatedBy(), u.getFirstName() + ' ' + u.getLastName());

            toReturnApiResponse.setDescription("Success");
            toReturnApiResponse.setStatus(200);
            toReturnApiResponse.setData(Collections.singletonList(toAttach));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return toReturnApiResponse;
    }

    public APIResponse getAllProcess() {
        APIResponse toReturnApiResponse = new APIResponse(404, "Error");
        List<ProcessDomain> toReturn = new ArrayList<>();
        try {
            List<Process> toConvert = this.processRepository.findAll();

            Integer processId;
            SysUser u = this.authPermissionService.getCurrentUser();
            for (Process temp : toConvert) {

                processId = temp.getProcessId();
                Integer size = Math.toIntExact(this.auditLogRepository.countLatestAuditLogByProcessId(processId));
                Integer count = this.auditLogRepository.countCompletedStepsWhereProcessId(processId);

                ProcessDomain toAttach = this.convertToProcessDomain(temp, size, count, temp.getCreatedBy(), u.getFirstName() + ' ' + u.getLastName());

                toReturn.add(toAttach);
            }

            toReturnApiResponse.setData(Collections.singletonList(toReturn));
            toReturnApiResponse.setStatus(200);
            toReturnApiResponse.setDescription("Success");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturnApiResponse;
    }


    public APIResponse getProcessStepList(Integer processId) {
        APIResponse toReturnApiResponse = new APIResponse(404, "Error");
        List<ProcessStepDomain> toAttach = new ArrayList<>();
        try {
            List<AuditLog> fromDbProcessStepModels = this.auditLogRepository.findLatestAuditLogByProcessId(processId);
            // DONETODO 2: return the DomainObject
            SysUser u = this.authPermissionService.getCurrentUser();
            for (AuditLog temp : fromDbProcessStepModels) {
                toAttach.add(this.convertToProcessStepDomain(temp, u.getFirstName() + ' ' + u.getLastName()));
            }
            toReturnApiResponse.setData(Collections.singletonList(toAttach));
            toReturnApiResponse.setStatus(200);
            toReturnApiResponse.setDescription("Success");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturnApiResponse;
    }

    public APIResponse getProcessStepListForDiagram(Integer processId) {
        APIResponse toReturnApiResponse = new APIResponse(404, "Error");
        List<ProcessStepDomain> toAttach = new ArrayList<>();
        try {
            List<AuditLog> fromDbProcessStepModels = this.auditLogRepository.findAllAuditLogByProcessId(processId);
            SysUser u = this.authPermissionService.getCurrentUser();
            for (AuditLog temp : fromDbProcessStepModels) {
                toAttach.add(this.convertToProcessStepDomain(temp, u.getFirstName() + ' ' + u.getLastName()));
            }
            toReturnApiResponse.setData(Collections.singletonList(toAttach));
            toReturnApiResponse.setStatus(200);
            toReturnApiResponse.setDescription("Success");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturnApiResponse;
    }

    public APIResponse saveComment(Integer stepId, String stepComments) {
        APIResponse toReturnApiResponse = new APIResponse(404, "Error");
        try {
            AuditLog temp = this.auditLogRepository.findById(stepId).get();
            if (temp.getStatus().getStatusId() == 8) {
                toReturnApiResponse.setStatus(500);
                toReturnApiResponse.setDescription("Cannot save Comment, As process is Stopped");
            } else {

                temp.setComments(stepComments);
                this.auditLogRepository.save(temp);
                Process toUpdateModifyDetails = this.processRepository.findById(temp.getProcess().getProcessId()).get();
                toUpdateModifyDetails.setModifiedOn(LocalDateTime.now());
                SysUser u = this.authPermissionService.getCurrentUser();
                toUpdateModifyDetails.setModifiedBy((int) u.getId());
                toReturnApiResponse.setData(Collections.singletonList(this.convertToProcessStepDomain(temp, u.getFirstName() + ' ' + u.getLastName())));
                toReturnApiResponse.setStatus(200);
                toReturnApiResponse.setDescription("SuccessFully Updated");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturnApiResponse;
    }

    // here flag represent the return data.
    // if 0 then return the normal api call.
    // if 1 then return only the next step in the return for the internal api calls.
    public APIResponse updateStatus(Integer stepId, Integer statusId, Integer flag) {
        APIResponse toReturnApiResponse = new APIResponse(404, "Error");
        try {
            //Fetching the AuditLog from the db.
            AuditLog temp = this.auditLogRepository.findById(stepId).get();
            // if status is already 8 (stopped), then we can't update the status
            if (temp.getStatus().getStatusId() == 8) {
                toReturnApiResponse.setStatus(500);
                toReturnApiResponse.setDescription("Cannot update status, As process is Stopped");
                return toReturnApiResponse;
            }
            // If the request is to update the status to 8 (stopped), only a step status cannot be changed
            // it should be done through other api.
            if (statusId == 8) {
                toReturnApiResponse.setStatus(500);
                toReturnApiResponse.setDescription("Cannot Apply Stop state to the Step, Only for WorkFlow");
                return toReturnApiResponse;
            }

            // fetch the latest record having process model and sequence.
            List<AuditLog> listOfNextAuditLogs = this.auditLogRepository.findLatestAuditLogByProcessId(temp.getProcess().getProcessId());
            AuditLog next = this.findNextAuditLogInList(listOfNextAuditLogs, stepId);
            // fetch the Process of the step whose status need to be updated.
            Process currentProcess = this.processRepository.findById(temp.getProcess().getProcessId()).get();
            // fetching the user from the token in the API.
            SysUser u = this.authPermissionService.getCurrentUser();
            // if next step status is completed then it wont allow the user to update the status.
            // checking whether there is next step present or not
            if ((next.getAuditId() != null)) {
                // we will only update the status if the next step is normal state, else don't change.
                if (next.getStatus().getStatusId() == 1) {
                    // updating the status of the step received.
                    AuditLog toSave = this.createNewAuditLogObject(temp, (int) u.getId());
                    toSave.setStatus(new Status(statusId));
                    this.auditLogRepository.save(toSave);
                    currentProcess.setStatusId(this.statusRepository.findById(statusId).get());
                    currentProcess.setModifiedBy((int) u.getId());
                    currentProcess.setModifiedOn(LocalDateTime.now());

                    // If status of current state status is to be set to completed,
                    // Then update the next status to started and update the deadline
                    if (statusId == 5) {
                        //updating the status to started
                        next.setStatus(new Status(2));
                        //fetch the deadline period and unit to update the deadline.
                        Step toFetchDeadlineFrom = this.stepRepository.findById(next.getStepId()).get();
                        //updating the deadline to required, from current time.
                        next.setDeadline(this.getDeadline(toFetchDeadlineFrom.getDeadlinePeriod(), toFetchDeadlineFrom.getDeadlineUnit()));
                        this.auditLogRepository.save(next);
                        currentProcess.setStatusId(this.statusRepository.findById(2).get());
                    }
                    toReturnApiResponse.setData(Collections.singletonList(this.convertToProcessStepDomain(temp, u.getFirstName() + ' ' + u.getLastName())));
                    toReturnApiResponse.setStatus(200);
                    toReturnApiResponse.setDescription("SuccessFully Updated");
                } else {
                    toReturnApiResponse.setStatus(500);
                    toReturnApiResponse.setDescription("Cannot Change the status, As next step is Already Started");
                }
            } // if the next step is not present just updating the status of the step received.
            else {

                AuditLog toSave = this.createNewAuditLogObject(temp, (int) u.getId());
                toSave.setStatus(new Status(statusId));
                this.auditLogRepository.save(toSave);
                currentProcess.setStatusId(this.statusRepository.findById(statusId).get());
                currentProcess.setModifiedBy((int) u.getId());
                currentProcess.setModifiedOn(LocalDateTime.now());

                toReturnApiResponse.setData(Collections.singletonList(this.convertToProcessStepDomain(temp, u.getFirstName() + ' ' + u.getLastName())));
                toReturnApiResponse.setStatus(200);
                toReturnApiResponse.setDescription("SuccessFully Updated");
            }
            this.processRepository.save(currentProcess);


            // if the api is called from the internal Micro-service then
            if (flag == 1) {
                UpdateAPIDomain toReturn = new UpdateAPIDomain();
                toReturn.setProcessId(currentProcess.getProcessId());

                // if there is next element present and the status to be updated is completed
                // then return the next stepId and statusId of the next step, else return the current stepId and statusId.
                if (next.getAuditId() != null && statusId == 5) {
                    toReturn.setStatusId(next.getStatus().getStatusId());
                    toReturn.setStepId(next.getAuditId());
                } else {
                    toReturn.setStatusId(temp.getStatus().getStatusId());
                    toReturn.setStepId(temp.getAuditId());
                }
                toReturnApiResponse.setData(Collections.singletonList(toReturn));
            }

        } catch (NullPointerException e) {
            toReturnApiResponse.setDescription("You have not set the deadline in the WorkFlow");
        } catch (Exception e) {
            toReturnApiResponse.setDescription("Some Exception Occurred, Check logs");
            e.printStackTrace();
        }

        return toReturnApiResponse;
    }

    public APIResponse updateStatusAPI(Integer stepId, Integer statusId) {
        APIResponse toReturnApiResponse = new APIResponse(404, "Error");
        toReturnApiResponse = this.updateStatus(stepId, statusId, 1);

        return toReturnApiResponse;

    }

    private AuditLog findNextAuditLogInList(List<AuditLog> listOfNextAuditLogs, Integer stepId) {
        AuditLog toReturn = new AuditLog();
        for (int i = 0; i < listOfNextAuditLogs.size(); i++) {
            if (listOfNextAuditLogs.get(i).getAuditId() == stepId) {
                if (i + 1 < listOfNextAuditLogs.size()) {
                    toReturn = listOfNextAuditLogs.get(i + 1);
                    break;
                }
            }
        }
        return toReturn;
    }


    public APIResponse getProcessStepList() {
        APIResponse toReturnApiResponse = new APIResponse(404, "Error");
        try {
            List<Status> toAttach = this.statusRepository.findAll();
            toReturnApiResponse.setStatus(200);
            toReturnApiResponse.setDescription("List Fetched");
            toReturnApiResponse.setData(Collections.singletonList(toAttach));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturnApiResponse;
    }

    public APIResponse stopProcess(Integer processId) {
        APIResponse toReturnApiResponse = new APIResponse(404, "Error");
        try {
            Process toStop = this.processRepository.findById(processId).get();
            toStop.setModifiedOn(LocalDateTime.now());
            SysUser u = this.authPermissionService.getCurrentUser();
            toStop.setModifiedBy((int) u.getId());
            toStop.setStatusId(this.statusRepository.findById(8).get());

            List<AuditLog> listOfNextAuditLogs = this.auditLogRepository.findLatestAuditLogByProcessId(toStop.getProcessId());

            for (AuditLog toStopAuditLogs : listOfNextAuditLogs) {
                AuditLog toSave = this.createNewAuditLogObject(toStopAuditLogs, (int) u.getId());
                toSave.setStatus(new Status(8));
                this.auditLogRepository.save(toSave);
            }

            toReturnApiResponse.setDescription("Process Stopped Successfully");
            toReturnApiResponse.setStatus(200);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturnApiResponse;
    }

    public AuditLog createNewAuditLogObject(AuditLog old, Integer userId) {
        AuditLog converted = new AuditLog();
        converted.setProcess(old.getProcess());
        converted.setStepId(old.getStepId());
        converted.setStatus(old.getStatus());
        converted.setCreatedOn(old.getCreatedOn());
        converted.setCreatedBy(old.getCreatedBy());
        converted.setComments(old.getComments());
        converted.setModifiedOn(LocalDateTime.now());
        converted.setModifiedBy(userId);
        converted.setDeadline(old.getDeadline());
        return converted;

    }

    public ProcessDomain convertToProcessDomain(Process toConvert, Integer steps, Integer stepsCompleted, Integer userId, String name) {
        ProcessDomain converted = new ProcessDomain();
        converted.setBluePrintId(toConvert.getWfTemplate().getWfTemplateId());
        converted.setProcessId(toConvert.getProcessId());
        converted.setUserId(userId);
        converted.setProcessName(toConvert.getName());
        converted.setProcessSteps(steps);

        converted.setUserName(name);
        converted.setStepsCompleted(stepsCompleted);
        converted.setLastStatus(toConvert.getStatusId().getName());
        return converted;
    }

    public AuditLog convertToAuditLogFromStep(Step toConvert, Integer processId, LocalDateTime currentDate, Integer createdBy) {
        AuditLog converted = new AuditLog();
        converted.setProcess(new Process(processId));
        converted.setStepId(toConvert.getStepId());
        converted.setStatus(new Status(1));
        converted.setCreatedBy(createdBy);
        converted.setCreatedOn(currentDate);
        converted.setModifiedBy(createdBy);
        converted.setModifiedOn(currentDate);
        return converted;
    }


    public ProcessStepDomain convertToProcessStepDomain(AuditLog toConvert, String name) {
        ProcessStepDomain converted = new ProcessStepDomain();
        //DONETODO 1: Complete the Method
        converted.setProcessStepId(toConvert.getAuditId());
        converted.setProcessId(toConvert.getProcess().getProcessId());
        //TODO: fetch the groupid from userid
        converted.setGroupId(1);
        converted.setGroupName("Sample");

        Step temp = this.stepRepository.findById(toConvert.getStepId()).get();
        converted.setProcessStepName(temp.getName());
        converted.setProcessDetail(temp.getDetails());

        converted.setStatus(toConvert.getStatus().getName());
        converted.setProcessComments(toConvert.getComments());

        //TODO:Check if it's used else remove from frontend and backend.
        converted.setWorkflowBluePrintStepId(temp.getWfTemplate().getWfTemplateId());
        if (toConvert.getDeadline() != null) {
            converted.setDeadline(toConvert.getDeadline());
        }
        //TODO:fetch the user name, using the api and assign the name here
        converted.setUpdatedBy(name);
        converted.setUpdatedOn(toConvert.getModifiedOn());

        return converted;
    }

    public LocalDateTime getDeadline(int period, String unit) {
        LocalDateTime toSend = LocalDateTime.now();
        if (unit.equals("Hour")) {
            toSend = toSend.plusHours(period);
        } else if (unit.equals("Days")) {
            toSend = toSend.plusDays(period);
        } else if (unit.equals("Months")) {
            toSend = toSend.plusMonths(period);
        }
        return toSend;
    }


}
