package com.workflowengine.workflowengine.controller;


import com.workflowengine.workflowengine.model.APIResponse;
import com.workflowengine.workflowengine.model.Credentials;
import com.workflowengine.workflowengine.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.workflowengine.workflowengine.utils.Constants.*;

@RestController
@RequestMapping(CONTROLLER_PATH_USER)
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(PATH_SAMPLE_LIST)
    APIResponse getSampleList(){
        return this.userService.getSampleList();
    }

    @PostMapping(PATH_SIGN_UP)
    public APIResponse signUp(@RequestBody Credentials user){
        return this.userService.signUp(user);
    }

}
