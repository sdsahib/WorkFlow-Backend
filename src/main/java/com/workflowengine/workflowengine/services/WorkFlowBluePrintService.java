package com.workflowengine.workflowengine.services;


import com.workflowengine.workflowengine.domain.*;
import com.workflowengine.workflowengine.model.*;
import com.workflowengine.workflowengine.repository.*;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.workflowengine.workflowengine.utils.Constants.*;

@Service
public class WorkFlowBluePrintService {



    @Autowired
    private WFTemplateRepository wfTemplateRepository;

    @Autowired
    private StepRepository stepRepository;

    @Autowired
    private StepAssigneeRepository stepAssigneeRepository;

    @Autowired
    private FlowRepository flowRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private AuthPermissionService authPermissionService;

    private final Logger logger = LoggerFactory.getLogger(WorkFlowBluePrintService.class);

    public APIResponse createNewProcessBluePrintService(String name) {
        APIResponse returnAPIResponse = new APIResponse(404, STRING_ERROR);
        WFTemplate wfTemplate = new WFTemplate();
        wfTemplate.setName(name);
        try {

            //TODODONE: fetch the userid From the api and save it in the WFUSER table.
            SysUser u = authPermissionService.getCurrentUser();


            if (u.getId() != 0) {

                wfTemplate.setCreatedBy((int) u.getId());
                wfTemplate.setModifiedBy((int) u.getId());
                wfTemplate.setCreatedOn(LocalDateTime.now());
                wfTemplate.setModifiedOn(LocalDateTime.now());
                WFTemplate saved = this.wfTemplateRepository.save(wfTemplate);
                returnAPIResponse.setStatus(200);
                returnAPIResponse.setDescription(STRING_SUCCESS);
                WorkFlowBluePrintDomain temporary = new WorkFlowBluePrintDomain();
                temporary.setId(saved.getWfTemplateId());
                temporary.setWorkFlowName(saved.getName());



                temporary.setUserName(u.getFirstName() + ' ' + u.getLastName());
                returnAPIResponse.setData(Collections.singletonList(new ArrayList<>(Arrays.asList(temporary))));

            }
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            returnAPIResponse.setStatus(500);
            returnAPIResponse.setDescription(STRING_ERROR + e.getMessage());
        }

        return returnAPIResponse;
    }


    public APIResponse getAllWorkFlowList() {
        APIResponse toReturnAPIResponse = new APIResponse(404, STRING_ERROR);

        try {
            List<WFTemplate> list = this.wfTemplateRepository.findAll();
            List<WorkFlowBluePrintDomain> domainResult = new ArrayList<>();
            WorkFlowBluePrintDomain temporary;
            for (WFTemplate a : list) {
                temporary = new WorkFlowBluePrintDomain();
                temporary.setId(a.getWfTemplateId());
                temporary.setWorkFlowName(a.getName());
                SysUser u = this.authPermissionService.getCurrentUser();
                temporary.setUserName(u.getFirstName() + ' ' + u.getLastName());
                domainResult.add(temporary);
            }

            toReturnAPIResponse.setData(Collections.singletonList(domainResult));
            toReturnAPIResponse.setStatus(200);
            toReturnAPIResponse.setDescription(STRING_SUCCESS);
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            toReturnAPIResponse.setDescription(STRING_ERROR + e.getMessage());

        }

        return toReturnAPIResponse;

    }

    public APIResponse createBluePrintStep(WorkFlowStepDomain workFlowStepDomain) {
        APIResponse toReturnApiResponse = new APIResponse(404, STRING_ERROR);

        Step workFlowStep = this.convertToStepFromWorkFlowStepDomain(workFlowStepDomain);
        try {
            Integer sequenceNumber = workFlowStepDomain.getSequenceNumber();
            //setting the active bit
            workFlowStep.setActive(true);
            workFlowStep.setDeadlinePeriod(3);
            workFlowStep.setDeadlineUnit(STRING_MONTHS);
            Step temp = this.stepRepository.save(workFlowStep);
            WorkFlowStepDomain tempToReturnDomain = this.convertToWorkFlowStepDomainFromStep(temp);
            tempToReturnDomain.setSequenceNumber(sequenceNumber);
            toReturnApiResponse.setData(Collections.singletonList(tempToReturnDomain));
            toReturnApiResponse.setStatus(200);
            toReturnApiResponse.setDescription(STRING_SUCCESS);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return toReturnApiResponse;
    }

    public APIResponse updateBluePrintStep(WorkFlowStepDomain workFlowStepDomain) {
        APIResponse toReturnApiResponse = new APIResponse(404, STRING_ERROR);

        //fetch the particular record from step table modify it and update it.
        //create an entry in the stepHistory table with updated as status
        Step workFlowBluePrintStepModel = this.convertToStepFromWorkFlowStepDomain(workFlowStepDomain);


        try {
            Integer sequenceNumber = workFlowStepDomain.getSequenceNumber();
            Step temp = this.stepRepository.findById(workFlowBluePrintStepModel.getStepId()).get();
            workFlowBluePrintStepModel.setStepId(temp.getStepId());
            workFlowBluePrintStepModel = this.stepRepository.save(workFlowBluePrintStepModel);

            WFTemplate toUpdateModified  =this.wfTemplateRepository.findById(workFlowBluePrintStepModel.getWfTemplate().getWfTemplateId()).get();
            toUpdateModified.setModifiedOn(LocalDateTime.now());
            SysUser u = authPermissionService.getCurrentUser();

            toUpdateModified.setModifiedBy((int) u.getId());
            this.wfTemplateRepository.save(toUpdateModified);
            WorkFlowStepDomain tempWorkFlowStepDomain = this.convertToWorkFlowStepDomainFromStep(workFlowBluePrintStepModel);
            tempWorkFlowStepDomain.setSequenceNumber(sequenceNumber);
            toReturnApiResponse.setData(Collections.singletonList(tempWorkFlowStepDomain));
            toReturnApiResponse.setStatus(200);
            toReturnApiResponse.setDescription(STRING_SUCCESS);

            //TODO create an entry in the history table if required
//            this.workFlowBluePrintStepHistoryRepository.save(this.convertToWorkFlowStepHistory(temp, "Updated"));

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return toReturnApiResponse;
    }


    public APIResponse getAllBluePrintSteps(Integer workflowId) {
        APIResponse toReturnApiResponse = new APIResponse(404, STRING_ERROR);
        List<Step> tempListToAttach;
        List<WorkFlowStepDomain> listToAttach = new ArrayList<>();
        try {
            WFTemplate wfTemplate = new WFTemplate(workflowId);
            tempListToAttach = this.stepRepository.findByWfTemplateAndActive(wfTemplate, true);
            for (Step e : tempListToAttach) {
                listToAttach.add(this.convertToWorkFlowStepDomainFromStep(e));
            }
            toReturnApiResponse.setData(Collections.singletonList(listToAttach));
            toReturnApiResponse.setStatus(200);
            toReturnApiResponse.setDescription(STRING_SUCCESS);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        toReturnApiResponse.setData(Collections.singletonList(listToAttach));
        return toReturnApiResponse;
    }

    public APIResponse getAllBluePrintStepList(Integer blueprintId) {
        APIResponse toReturnApiResponse = new APIResponse(404, STRING_ERROR);
        List<Step> tempListToAttach;
        List<WorkFlowStepListDomain> listToAttach = new ArrayList<>();
        try {
            WFTemplate workFlowBluePrintModel = new WFTemplate(blueprintId);
            tempListToAttach = this.stepRepository.findByWfTemplateAndActive(workFlowBluePrintModel, true);
            for (Step temp : tempListToAttach) {
                listToAttach.add(this.convertToWorkFlowStepListDomainFromStep(temp));
            }
            toReturnApiResponse.setDescription(STRING_SUCCESS);
            toReturnApiResponse.setData(Collections.singletonList(listToAttach));
            toReturnApiResponse.setStatus(200);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return toReturnApiResponse;

    }


    public APIResponse deleteWorkFlowBluePrintStepByWorkFlowId(Integer stepId ) {
        APIResponse toReturnApiResponse = new APIResponse(404, STRING_ERROR);
        try {

            //fetching the record and setting the bit to be inactive
            Step toDelete = this.stepRepository.findById(stepId).get();
            toDelete.setActive(false);
            this.stepRepository.save(toDelete);

            //updating the modified details
            WFTemplate toUpdateModifiedDetails = this.wfTemplateRepository.findById(toDelete.getWfTemplate().getWfTemplateId()).get();
            toUpdateModifiedDetails.setModifiedOn(LocalDateTime.now());
            SysUser u = authPermissionService.getCurrentUser();
            toUpdateModifiedDetails.setModifiedBy((int) u.getId());
            this.wfTemplateRepository.save(toUpdateModifiedDetails);

            toReturnApiResponse.setStatus(200);
            toReturnApiResponse.setDescription(STRING_SUCCESS);
        } catch (ConstraintViolationException e) {
            // This row trying to delete is being refrences as foreign can nnot be deleted.
            toReturnApiResponse.setDescription(STRING_ERROR);

        } catch (Exception e) {
            //replace with proper Exception and Logger
            logger.error(e.getMessage());
        }

        return toReturnApiResponse;
    }

    public APIResponse assignBluePrintStepToGroup(StepAssigneeDomain stepAssigneeDomain) {
        APIResponse toReturnApiResponse = new APIResponse(404, STRING_ERROR);
        try {

            for (int i = 0; i < stepAssigneeDomain.getGroupModelList().size(); i++) {
                this.stepAssigneeRepository.deleteByStepId(stepAssigneeDomain.getStepId());
            }

            for (int i = 0; i < stepAssigneeDomain.getGroupModelList().size(); i++) {
                StepAssignee temp = new StepAssignee();
                temp.setStepAssigneeIdentity(new StepAssigneeIdentity(stepAssigneeDomain.getStepId(), stepAssigneeDomain.getGroupModelList().get(i).getId()));
                this.stepAssigneeRepository.save(temp);
            }

            Step temp = this.stepRepository.findById(stepAssigneeDomain.getStepId()).get();
            WFTemplate toUpdateModifiedBy = this.wfTemplateRepository.findById(temp.getWfTemplate().getWfTemplateId()).get();
            SysUser u = authPermissionService.getCurrentUser();
            toUpdateModifiedBy.setModifiedBy((int) u.getId());
            toUpdateModifiedBy.setModifiedOn(LocalDateTime.now());
            this.wfTemplateRepository.save(toUpdateModifiedBy);
            toReturnApiResponse.setStatus(200);
            toReturnApiResponse.setDescription(STRING_SUCCESS);
            toReturnApiResponse.setData(Collections.singletonList(stepAssigneeDomain));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return toReturnApiResponse;

    }


    public APIResponse getAssigneeBluePrintStepDetails(Integer stepId) {
        APIResponse toReturnApiResponse = new APIResponse(404, STRING_ERROR);
        try {
            //getting the step id so that we can find in the table the corresponding assignee detail.
            List<StepAssignee> fetched = this.stepAssigneeRepository.findByStepId(stepId);
            StepAssigneeDomain converted = new StepAssigneeDomain();
            converted.setStepId(stepId);
            for (StepAssignee e : fetched) {
                //TODO: CAll the api and fetch the Role info and assign to Role domain.
                RoleDomain a = new RoleDomain(1,"Sample Role");
                List<RoleDomain> temp = converted.getGroupModelList();
                if (temp == null) {
                    temp = new ArrayList<>();
                }
                temp.add(a);
                converted.setGroupModelList(temp);
            }

            toReturnApiResponse.setStatus(200);
            toReturnApiResponse.setDescription(STRING_SUCCESS);
            toReturnApiResponse.setData(Collections.singletonList(converted));

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return toReturnApiResponse;
    }


    public APIResponse createWorkFlowStepFlow(Integer stepId, Integer statusId, Integer childStepId) {
        APIResponse toReturnApiResponse = new APIResponse(404, STRING_ERROR);
        try {
            //WorkFlowBluePrintStepFlowDomain
            FlowIdentity temp = new FlowIdentity(stepId, statusId, childStepId);
            Flow flow = this.flowRepository.save(new Flow(temp));

            Step toFetchWFTemplateIdFrom = this.stepRepository.findById(stepId).get();
            WFTemplate toUpdatedModifiedDetail = this.wfTemplateRepository.findById(toFetchWFTemplateIdFrom.getWfTemplate().getWfTemplateId()).get();
            toUpdatedModifiedDetail.setModifiedOn(LocalDateTime.now());
            SysUser u = authPermissionService.getCurrentUser();
            toUpdatedModifiedDetail.setModifiedBy((int) u.getId());
            this.wfTemplateRepository.save(toUpdatedModifiedDetail);
            WorkFlowBluePrintStepFlowDomain temp1 = this.convertToWorkFlowBluePrintStepFlowDomainFromFlow(flow);

            toReturnApiResponse.setDescription(STRING_SUCCESS);
            toReturnApiResponse.setStatus(200);
            toReturnApiResponse.setData(Collections.singletonList(temp1));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return toReturnApiResponse;
    }

    public WorkFlowBluePrintStepFlowDomain convertToWorkFlowBluePrintStepFlowDomainFromFlow(Flow toConvert) {
        WorkFlowBluePrintStepFlowDomain converted = new WorkFlowBluePrintStepFlowDomain();
        converted.setStepId(toConvert.getFlowIdentity().getStepId());
        converted.setChildStepId(toConvert.getFlowIdentity().getChildStepId());
        converted.setStatusId(toConvert.getFlowIdentity().getStatusId());
        return converted;
    }

    public APIResponse getAllFlowsByStep(Integer stepId) {
        APIResponse toReturnApiResponse = new APIResponse(404, STRING_ERROR);

        try {
            List<Flow> fetchedList = this.flowRepository.findByStepId(stepId);

            List<WorkFlowBluePrintStepFlowDomain> convertedList = new ArrayList<>();

            for (Flow temp : fetchedList) {
                convertedList.add(this.convertToWorkFlowBluePrintStepFlowDomainFromFlow(temp));
            }

            toReturnApiResponse.setDescription(STRING_SUCCESS);
            toReturnApiResponse.setStatus(200);
            toReturnApiResponse.setData(Collections.singletonList(convertedList));

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return toReturnApiResponse;
    }

    public APIResponse deleteFlowOfBluePrintStep(Integer stepId, Integer statusId, Integer childStatusId) {
        APIResponse toReturnApiResponse = new APIResponse(404, STRING_ERROR);
        try {
            this.flowRepository.deleteById(new FlowIdentity(stepId, statusId, childStatusId));
            Step toFetchWFTemplateIdFrom = this.stepRepository.findById(stepId).get();
            WFTemplate toUpdateModifiedDetails = this.wfTemplateRepository.findById(toFetchWFTemplateIdFrom.getWfTemplate().getWfTemplateId()).get();
            SysUser u = authPermissionService.getCurrentUser();
            toUpdateModifiedDetails.setModifiedBy((int) u.getId());
            toUpdateModifiedDetails.setModifiedOn(LocalDateTime.now());
            this.wfTemplateRepository.save(toUpdateModifiedDetails);
            toReturnApiResponse.setDescription(STRING_SUCCESS);
            toReturnApiResponse.setStatus(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return toReturnApiResponse;
    }


    public APIResponse updateDeadline(Integer stepId, String deadline, String period) {

        APIResponse toReturnApiResponse = new APIResponse(404, STRING_ERROR);
        try {
            Step toUpdate = this.stepRepository.findById(stepId).get();
            toUpdate.setDeadlinePeriod(Integer.valueOf(deadline));
            toUpdate.setDeadlineUnit(period);

            this.stepRepository.save(toUpdate);
            WFTemplate toUpdatedModifiedDetails = this.wfTemplateRepository.findById(toUpdate.getWfTemplate().getWfTemplateId()).get();
            toUpdatedModifiedDetails.setModifiedOn(LocalDateTime.now());
            SysUser u = authPermissionService.getCurrentUser();
            toUpdatedModifiedDetails.setModifiedBy((int) u.getId());
            this.wfTemplateRepository.save(toUpdatedModifiedDetails);
            WorkFlowStepDomain temp = this.convertToWorkFlowStepDomainFromStep(toUpdate);
            toReturnApiResponse.setStatus(200);
            toReturnApiResponse.setDescription(STRING_SUCCESS);
            toReturnApiResponse.setData(Collections.singletonList(temp));

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return toReturnApiResponse;
    }

    public APIResponse getProcessStepList() {
        APIResponse toReturnApiResponse = new APIResponse(404, STRING_ERROR);
        try {
            List<Status> toAttach = this.statusRepository.findAll();
            toReturnApiResponse.setStatus(200);
            toReturnApiResponse.setDescription(STRING_SUCCESS);
            toReturnApiResponse.setData(Collections.singletonList(toAttach));

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return toReturnApiResponse;
    }


//    public APIResponse getAllProcessFlows(Integer processId) {
//        APIResponse toReturnApiResponse = new APIResponse(404, STRING_ERROR);
//        try {
//            List<Flow> toAttach = this.flowRepository.findAllByProcessIdOrderByStepId(processId);
//        }catch (Exception e){
//
//        }
//        return toReturnApiResponse;
//    }

    Step convertToStepFromWorkFlowStepDomain(WorkFlowStepDomain toConvert) {
        Step converted = new Step();
        converted.setStepId(toConvert.getId());
        converted.setName(toConvert.getName());
        converted.setDetails(toConvert.getDetails());
        converted.setWfTemplate(new WFTemplate(toConvert.getWorkFlowBluePrintId()));
        converted.setDeadlinePeriod(toConvert.getDeadline());
        converted.setDeadlineUnit(toConvert.getPeriod());

        return converted;
    }

    private WorkFlowStepDomain convertToWorkFlowStepDomainFromStep(Step toConvert) {
        WorkFlowStepDomain converted = new WorkFlowStepDomain();
        converted.setId(toConvert.getStepId());
        converted.setWorkFlowBluePrintId(toConvert.getWfTemplate().getWfTemplateId());
        //TODO remove the group from the Domain
        converted.setGroupId(1);
        converted.setGroupName("Sample");

        converted.setName(toConvert.getName());
        converted.setDetails(toConvert.getDetails());
        converted.setDeadline(toConvert.getDeadlinePeriod());
        converted.setPeriod(toConvert.getDeadlineUnit());
        return converted;
    }

    private WorkFlowStepListDomain convertToWorkFlowStepListDomainFromStep(Step toConvert) {
        WorkFlowStepListDomain toReturn = new WorkFlowStepListDomain();
        toReturn.setId(toConvert.getStepId());
        toReturn.setName(toConvert.getName());
        return toReturn;
    }


}
