package com.workflowengine.workflowengine.controller;

import com.workflowengine.workflowengine.model.APIResponse;
import com.workflowengine.workflowengine.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @GetMapping("/sampleEmail")
    private APIResponse sendEmail(){
        return this.emailService.sendSampleEmail();
    }
}
