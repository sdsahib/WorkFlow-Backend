package com.workflowengine.workflowengine.services;


import com.workflowengine.workflowengine.security.AuthHolder;
import com.workflowengine.workflowengine.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Set;

@Component
public class ServiceLocator {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private HttpHeaders headers;

    private String baseUrl = "http://localhost:8080/ghgateway";


    public ServiceLocator() {
    }

    public ResponseEntity getData(String url, Map<String, Object> urlParams, Class clazz) {
        headers.set(Constants.KEY_HEADER_AUTHORIZATION, "Bearer " + AuthHolder.getCurrentToken().getAccessToken());
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        ResponseEntity responseEntity;
        responseEntity = restTemplate.exchange(getUrl(url, urlParams), HttpMethod.GET, httpEntity, clazz);
        return responseEntity;
    }

    private String getUrl(String path, Map<String, Object> urlParams) {
        Set<String> keys = urlParams.keySet();
        String strParams = "";
        for (String key : keys) {
            strParams += strParams != "" ? "&" : "?";
            strParams += String.format("%s=%s", key, urlParams.get(key));
        }
        return baseUrl + "/ghsystem/ghsystem" + path + strParams;
    }

}
