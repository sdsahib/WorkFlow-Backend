package com.workflowengine.workflowengine.controller;

import com.workflowengine.workflowengine.domain.StepAssigneeDomain;
import com.workflowengine.workflowengine.domain.WorkFlowStepDomain;
import com.workflowengine.workflowengine.model.APIResponse;
import com.workflowengine.workflowengine.services.WorkFlowBluePrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.workflowengine.workflowengine.utils.Constants.*;

@RestController
@RequestMapping(CONTROLLER_PATH_PROCESS_BLUEPRINT)
public class ProcessBluePrintController {
    @Autowired
    private WorkFlowBluePrintService workFlowBluePrintService;

    /**
     * Controller for creating the WorkFlow Blueprint
     *
     * @param name
     * @return APIResponse
     */
    @PostMapping(PATH_CREATE_BLUEPRINT)
    private APIResponse print(@RequestParam String name) {

        return workFlowBluePrintService.createNewProcessBluePrintService(name);
    }

    /**
     * REST Controller for fetching all workflows details
     *
     * @return APIResponse
     */
    @GetMapping(PATH_ALL_WORKFLOW_LIST)
    public APIResponse getAllWorkflowList() {
        return workFlowBluePrintService.getAllWorkFlowList();
    }

    @PostMapping(PATH_CREATE_BLUEPRINT_STEP)
    public APIResponse createBluePrintStep(@RequestBody WorkFlowStepDomain workFlowStepDomain) {

        return workFlowBluePrintService.createBluePrintStep(workFlowStepDomain);
    }

    @PutMapping(PATH_UPDATE_BLUEPRINT_STEP)
    public APIResponse updateBluePrintStep(@RequestBody WorkFlowStepDomain workFlowStepDomain) {

        return workFlowBluePrintService.updateBluePrintStep(workFlowStepDomain);
    }

    @GetMapping(PATH_GET_ALL_BLUEPRINT_STEPS)
    public APIResponse getAllBluePrintStep(@RequestParam Integer workflowId) {
        return workFlowBluePrintService.getAllBluePrintSteps(workflowId);
    }

    @DeleteMapping(PATH_DELETE_BLUEPRINT_STEP)
    public APIResponse deleteWorkFlowBluePrintStepByWorkFlowId(@RequestParam Integer stepId) {
        return this.workFlowBluePrintService.deleteWorkFlowBluePrintStepByWorkFlowId(stepId);

    }

    @PostMapping(PATH_ASSIGN_BLUEPRINT_STEP)
    public APIResponse assignBluePrintStep(@RequestBody StepAssigneeDomain stepAssigneeDomain) {
        return this.workFlowBluePrintService.assignBluePrintStepToGroup(stepAssigneeDomain);
    }

    @GetMapping(PATH_GET_ASSIGN_BLUEPRINT_DETAILS)
    public APIResponse getAssigneeBluePrintStep(@RequestParam Integer stepId) {
        return this.workFlowBluePrintService.getAssigneeBluePrintStepDetails(stepId);
    }

    @PostMapping(PATH_CREATE_WORKFLOW_STEP_FLOW)
    public APIResponse createWorkFlowStepFlow(@RequestParam Integer stepId, @RequestParam Integer statusId,
                                              @RequestParam Integer childStepId) {
        return this.workFlowBluePrintService.createWorkFlowStepFlow(stepId, statusId, childStepId);
    }

    @PostMapping(PATH_GET_ALL_FLOWS_OF_STEP)
    public APIResponse getAllFlowsOfStep(@RequestParam Integer stepId) {
        return this.workFlowBluePrintService.getAllFlowsByStep(stepId);
    }

    @DeleteMapping(PATH_DELETE_FLOW_OF_STEP)
    public APIResponse deleteFlowOfBluePrintStep(@RequestParam Integer stepId, @RequestParam Integer statusId,
                                                 @RequestParam Integer childStepId) {
        return this.workFlowBluePrintService.deleteFlowOfBluePrintStep(stepId,statusId, childStepId);
    }

    @PostMapping(PATH_UPDATE_DEADLINE)
    public APIResponse updateDeadline(@RequestParam Integer stepId, @RequestParam String deadline,
                                      @RequestParam String period) {

        return this.workFlowBluePrintService.updateDeadline(stepId, deadline, period);
    }

    @GetMapping(PATH_GET_STEPS_LIST)
    public APIResponse getStepList(@RequestParam Integer blueprintId) {
        return this.workFlowBluePrintService.getAllBluePrintStepList(blueprintId);
    }

    @GetMapping(PATH_GET_PROCESS_STATUS_LIST)
    public APIResponse getProcessStepList() {
        return this.workFlowBluePrintService.getProcessStepList();
    }

//    @GetMapping("/getAllProcessFlows")
//    public APIResponse getAllProcessFlows(@RequestParam Integer processId){
//        return this.workFlowBluePrintService.getAllProcessFlows(processId);
//    }

}
