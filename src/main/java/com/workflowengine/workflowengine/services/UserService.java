package com.workflowengine.workflowengine.services;

import com.workflowengine.workflowengine.domain.GroupDomain;
import com.workflowengine.workflowengine.model.APIResponse;
import com.workflowengine.workflowengine.model.Credentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {


    @Autowired
    private ServiceLocator serviceLocator;


    private BCryptPasswordEncoder bCryptPasswordEncoder;

//    @Autowired
//    private CredentialsRepository credentialsRepository;

    public APIResponse getSampleList() {
        APIResponse toReturnApiResponse = new APIResponse(404, "Error");
        try {
            List<GroupDomain> temp = this.getAllRole();

            toReturnApiResponse.setData(Collections.singletonList(temp));
            toReturnApiResponse.setStatus(200);
            toReturnApiResponse.setDescription("Success");
        }catch (Exception e){
            e.printStackTrace();
        }

        return toReturnApiResponse;
    }

    public List<GroupDomain> getAllRole(){
        Map<String, Object> params = new HashMap<>();
        return (List<GroupDomain>) serviceLocator.getData("/role/getAll", params, List.class).getBody();
    }

    //TODO:


    public APIResponse signUp(Credentials user) {
        APIResponse toReturnApiResponse = new APIResponse(404, "Error");
        try{
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            //TODO: uncomment this when tables are there in the db
//            this.credentialsRepository.save(user);
        }catch(Exception e){
            toReturnApiResponse.setStatus(500);
            toReturnApiResponse.setDescription("Internal Server Error");
        }
        return toReturnApiResponse;
    }
}

