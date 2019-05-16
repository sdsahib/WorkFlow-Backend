package com.workflowengine.workflowengine.services;

import com.workflowengine.workflowengine.model.APIResponse;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

//    @Autowired
//    public JavaMailSender emailSender;

    public APIResponse sendSampleEmail() {
        APIResponse toReturnApiResponse = new APIResponse(404, "Error");
//        try{
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setTo("sdsahib95@gmail.com");
//            message.setSubject("testing email");
//            message.setText("Sample Email Worry NOt");
//            emailSender.send(message);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
        return toReturnApiResponse;
    }
}
