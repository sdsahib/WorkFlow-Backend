package com.workflowengine.workflowengine.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
public class APIResponse {
    private int status;
    private String description;
    private List<Object> data;

    public APIResponse(int status, String description) {
        this.status = status;
        this.description = description;
        this.data = null;
    }

}
