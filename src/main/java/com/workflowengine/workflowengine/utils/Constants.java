package com.workflowengine.workflowengine.utils;

public class Constants {

    public static final String CONTROLLER_PATH_EMAIL = "/email";
    public static final String PATH_EMAIL = "/sampleEmail";

    public static final String CONTROLLER_PATH_PROCESS_BLUEPRINT = "/processblueprint";
    public static final String PATH_CREATE_BLUEPRINT = "/createBlueprint";
    public static final String PATH_ALL_WORKFLOW_LIST = "/allworkflowlist";
    public static final String PATH_CREATE_BLUEPRINT_STEP = "/createBluePrintStep";
    public static final String PATH_UPDATE_BLUEPRINT_STEP = "/updateBluePrintStep";
    public static final String PATH_GET_ALL_BLUEPRINT_STEPS = "/getAllBluePrintSteps";
    public static final String PATH_DELETE_BLUEPRINT_STEP = "/deleteBluePrintStep";
    public static final String PATH_ASSIGN_BLUEPRINT_STEP = "/assignBluePrintStep";
    public static final String PATH_GET_ASSIGN_BLUEPRINT_DETAILS = "/getassigneeDetails";
    public static final String PATH_CREATE_WORKFLOW_STEP_FLOW = "/createWorkFlowStepFlow";
    public static final String PATH_GET_ALL_FLOWS_OF_STEP = "/getAllFlowsOfStep";
    public static final String PATH_DELETE_FLOW_OF_STEP = "/deleteFlowOfBluePrintStep";
    public static final String PATH_UPDATE_DEADLINE = "/updateDeadline";
    public static final String PATH_GET_STEPS_LIST = "/getStepsList";
    public static final String PATH_GET_PROCESS_STATUS_LIST = "/getProcessStatusList";


    public static final String CONTROLLER_PATH_PROCESS = "/process";
    public static final String PATH_LAUNCH = "/launch";
    public static final String PATH_GET_ALL_PROCESS = "/getAllProcess";
    public static final String PATH_GET_PROCESS_LIST = "/getProcessList";
    public static final String PATH_GET_PROCESS_LIST_FOR_DIAGRAM = "/getProcessListForDiagram";
    public static final String PATH_GET_PROCESS_DETAILS = "/getProcessDetails";
    public static final String PATH_SAVE_COMMENT = "/saveComment";
    public static final String PATH_UPDATE_STATUS = "/updateStatus";
    public static final String PATH_UPDATE_STATUS_API = "/updateStatusApi";
    public static final String PATH_STOP_PROCESS = "/stopProcess";

    public static final String CONTROLLER_PATH_USER = "/user";
    public static final String PATH_SAMPLE_LIST = "/sampleList";
    public static final String PATH_SIGN_UP = "/sign-up";


    public static final String STRING_ERROR = "Error";
    public static final String STRING_SUCCESS = "Success";
    public static final String STRING_HOUR = "Hour";
    public static final String STRING_DAYS = "Days";
    public static final String STRING_MONTHS = "Months";

    public static final String STRING_PROCESS_CREATED = "Process Created";
    public static final String STRING_PROCESS_STEP_CREATED = "Process Step Created";
    public static final String STRING_CANNOT_SAVE = "Cannot save Comment, As process is Stopped";
    public static final String STRING_CANNOT_UPDATE_STATUS = "Cannot update status, As process is Stopped";
    public static final String STRING_CANNOT_APPLY = "Cannot Apply Stop state to the Step, Only for WorkFlow";
    public static final String STRING_CANNOT_CHANGE_STATUS = "Cannot Change the status, As next step is Already Started";
    public static final String STRING_SET_DEADLINE = "You have not set the deadline in the WorkFlow";
    public static final String STRING_SOME_EXCEPTION = "Some Exception Occurred, Check logs";



    public static final String KEY_HEADER_CONTENT_TYPE = "Content-Type";
    public static final String KEY_HEADER_AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer";

    public static final String VAL_HEADER_APPLICATION_JSON = "application/json";
    public static final String ORIGIN = "origin";



}
