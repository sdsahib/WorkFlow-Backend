package com.workflowengine.workflowengine.controller;

import com.workflowengine.workflowengine.model.APIResponse;
import com.workflowengine.workflowengine.services.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.workflowengine.workflowengine.utils.Constants.*;

@RestController
@RequestMapping(CONTROLLER_PATH_PROCESS)
public class ProcessController {
    @Autowired
    private ProcessService processService;


    @GetMapping(PATH_LAUNCH)
    private APIResponse createProcess(@RequestParam Integer workFlowId,
                                          @RequestParam String processName) {
        return this.processService.createProcess(workFlowId, processName);
    }

    @GetMapping(PATH_GET_ALL_PROCESS)
    private APIResponse getAllProcess() {
        return this.processService.getAllProcess();
    }

    @GetMapping(PATH_GET_PROCESS_LIST)
    private APIResponse getProcessList(@RequestParam Integer processId) {
        return this.processService.getProcessStepList(processId);
    }
    @GetMapping(PATH_GET_PROCESS_LIST_FOR_DIAGRAM)
    public APIResponse getProcessListForDiagram(@RequestParam Integer processId){
        return this.processService.getProcessStepListForDiagram(processId);
    }

    @GetMapping(PATH_GET_PROCESS_DETAILS)
    private APIResponse getProcessDetails(@RequestParam Integer processId) {
        return this.processService.getProcessDetails(processId);
    }

    @PostMapping(PATH_SAVE_COMMENT)
    private APIResponse saveComment(@RequestParam Integer stepId, @RequestParam String stepComments) {
        return this.processService.saveComment(stepId, stepComments);
    }

    @PostMapping(PATH_UPDATE_STATUS)
    private APIResponse updateStatus(@RequestParam Integer stepId, @RequestParam Integer statusID) {
        return this.processService.updateStatus(stepId, statusID,0);
    }

    /**
     * GET API for calling from the other Micro Service
     * @param stepId
     * @param statusID
     * @return
     */
    @GetMapping(PATH_UPDATE_STATUS_API)
    private APIResponse updateStatusAPI(@RequestParam Integer stepId, @RequestParam Integer statusID){
        return this.processService.updateStatusAPI(stepId, statusID);
    }

    @GetMapping(PATH_GET_PROCESS_STATUS_LIST)
    public APIResponse getProcessStepList() {
        return this.processService.getProcessStepList();
    }

    @PostMapping(PATH_STOP_PROCESS)
    public APIResponse stopProcess(@RequestParam Integer processId){
        return this.processService.stopProcess(processId);
    }

}
