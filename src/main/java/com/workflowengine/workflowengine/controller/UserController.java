package com.workflowengine.workflowengine.controller;


import com.workflowengine.workflowengine.model.APIResponse;
import com.workflowengine.workflowengine.model.Credentials;
import com.workflowengine.workflowengine.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;



    @GetMapping("/sampleList")
    APIResponse getSampleList(){
        return this.userService.getSampleList();
    }

    @PostMapping("/sign-up")
    public APIResponse signUp(@RequestBody Credentials user){
        return this.userService.signUp(user);
    }


}
