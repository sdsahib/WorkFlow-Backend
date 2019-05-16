package com.workflowengine.workflowengine.controller;

import com.workflowengine.workflowengine.model.APIResponse;
import com.workflowengine.workflowengine.services.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/process")
public class ProcessController {
    @Autowired
    private ProcessService processService;


    @GetMapping("/launch")
    private APIResponse createProcess(@RequestParam Integer workFlowId,
                                      @RequestParam String processName) {
        return this.processService.createProcess(workFlowId, processName);
    }

    @GetMapping("/getAllProcess")
    private APIResponse getAllProcess() {
        return this.processService.getAllProcess();
    }

    @GetMapping("/getProcessList")
    private APIResponse getProcessList(@RequestParam Integer processId) {
        return this.processService.getProcessStepList(processId);
    }
    @GetMapping("/getProcessListForDiagram")
    public APIResponse getProcessListForDiagram(@RequestParam Integer processId){
        return this.processService.getProcessStepListForDiagram(processId);
    }

    @GetMapping("/getProcessDetails")
    private APIResponse getProcessDetails(@RequestParam Integer processId) {
        return this.processService.getProcessDetails(processId);
    }

    @PostMapping("/saveComment")
    private APIResponse saveComment(@RequestParam Integer stepId, @RequestParam String stepComments) {
        return this.processService.saveComment(stepId, stepComments);
    }

    @PostMapping("/updateStatus")
    private APIResponse updateStatus(@RequestParam Integer stepId, @RequestParam Integer statusID) {
        return this.processService.updateStatus(stepId, statusID,0);
    }

    /**
     * GET API for calling from the other Micro Service
     * @param stepId
     * @param statusID
     * @return
     */
    @GetMapping("/updateStatusApi")
    private APIResponse updateStatusAPI(@RequestParam Integer stepId, @RequestParam Integer statusID){
        return this.processService.updateStatusAPI(stepId, statusID);
    }

    @GetMapping("/getProcessStatusList")
    public APIResponse getProcessStepList() {
        return this.processService.getProcessStepList();
    }

    @PostMapping("/stopProcess")
    public APIResponse stopProcess(@RequestParam Integer processId){
        return this.processService.stopProcess(processId);
    }

}
