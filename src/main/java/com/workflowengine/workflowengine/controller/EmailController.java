package com.workflowengine.workflowengine.controller;

import com.workflowengine.workflowengine.model.APIResponse;
import com.workflowengine.workflowengine.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.workflowengine.workflowengine.utils.Constants.CONTROLLER_PATH_EMAIL;
import static com.workflowengine.workflowengine.utils.Constants.PATH_EMAIL;

@RestController
@RequestMapping(CONTROLLER_PATH_EMAIL)
public class EmailController {
    @Autowired
    private EmailService emailService;

    @GetMapping(PATH_EMAIL)
    private APIResponse sendEmail(){
        return this.emailService.sendSampleEmail();
    }
}
