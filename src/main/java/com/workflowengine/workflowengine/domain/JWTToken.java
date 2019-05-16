package com.workflowengine.workflowengine.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JWTToken {

    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private long expiration;

    private String error;
    private String error_description;

    private HashMap<String, String> additionalAttributes;


}
