package com.workflowengine.workflowengine.controller;

import com.workflowengine.workflowengine.domain.StepAssigneeDomain;
import com.workflowengine.workflowengine.domain.WorkFlowStepDomain;
import com.workflowengine.workflowengine.model.APIResponse;
import com.workflowengine.workflowengine.services.WorkFlowBluePrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/processblueprint")
public class ProcessBluePrintController {
    @Autowired
    private WorkFlowBluePrintService workFlowBluePrintService;

    /**
     * Controller for creating the WorkFlow Blueprint
     *
     * @param name
     * @return APIResponse
     */
    @PostMapping("/createBlueprint")
    private APIResponse print(@RequestParam String name) {

        return workFlowBluePrintService.createNewProcessBluePrintService(name);
    }

    /**
     * REST Controller for fetching all workflows details
     *
     * @return APIResponse
     */
    @GetMapping("/allworkflowlist")
    public APIResponse getAllWorkflowList() {
        return workFlowBluePrintService.getAllWorkFlowList();
    }

    @PostMapping("/createBluePrintStep")
    public APIResponse createBluePrintStep(@RequestBody WorkFlowStepDomain workFlowStepDomain) {

        return workFlowBluePrintService.createBluePrintStep(workFlowStepDomain);
    }

    @PutMapping("/updateBluePrintStep")
    public APIResponse updateBluePrintStep(@RequestBody WorkFlowStepDomain workFlowStepDomain) {

        return workFlowBluePrintService.updateBluePrintStep(workFlowStepDomain);
    }

    @GetMapping("/getAllBluePrintSteps")
    public APIResponse getAllBluePrintStep(@RequestParam Integer workflowId) {
        return workFlowBluePrintService.getAllBluePrintSteps(workflowId);
    }

    @DeleteMapping("/deleteBluePrintStep")
    public APIResponse deleteWorkFlowBluePrintStepByWorkFlowId(@RequestParam Integer stepId) {
        return this.workFlowBluePrintService.deleteWorkFlowBluePrintStepByWorkFlowId(stepId);

    }

    @PostMapping("/assignBluePrintStep")
    public APIResponse assignBluePrintStep(@RequestBody StepAssigneeDomain stepAssigneeDomain) {
        return this.workFlowBluePrintService.assignBluePrintStepToGroup(stepAssigneeDomain);
    }

    @GetMapping("/getassigneeDetails")
    public APIResponse getAssigneeBluePrintStep(@RequestParam Integer stepId) {
        return this.workFlowBluePrintService.getAssigneeBluePrintStepDetails(stepId);
    }

    @PostMapping("/createWorkFlowStepFlow")
    public APIResponse createWorkFlowStepFlow(@RequestParam Integer stepId, @RequestParam Integer statusId,
                                              @RequestParam Integer childStepId) {
        return this.workFlowBluePrintService.createWorkFlowStepFlow(stepId, statusId, childStepId);

    }

    @PostMapping("/getAllFlowsOfStep")
    public APIResponse getAllFlowsOfStep(@RequestParam Integer stepId) {
        return this.workFlowBluePrintService.getAllFlowsByStep(stepId);
    }

    @DeleteMapping("/deleteFlowOfBluePrintStep")
    public APIResponse deleteFlowOfBluePrintStep(@RequestParam Integer stepId, @RequestParam Integer statusId,
                                                 @RequestParam Integer childStepId) {
        return this.workFlowBluePrintService.deleteFlowOfBluePrintStep(stepId,statusId, childStepId);
    }

    @PostMapping("/updateDeadline")
    public APIResponse updateDeadline(@RequestParam Integer stepId, @RequestParam String deadline,
                                      @RequestParam String period) {

        return this.workFlowBluePrintService.updateDeadline(stepId, deadline, period);
    }

    @GetMapping("/getStepsList")
    public APIResponse getStepList(@RequestParam Integer blueprintId) {
        return this.workFlowBluePrintService.getAllBluePrintStepList(blueprintId);
    }

    @GetMapping("/getProcessStatusList")
    public APIResponse getProcessStepList() {
        return this.workFlowBluePrintService.getProcessStepList();
    }

//    @GetMapping("/getAllProcessFlows")
//    public APIResponse getAllProcessFlows(@RequestParam Integer processId){
//        return this.workFlowBluePrintService.getAllProcessFlows(processId);
//    }

}
