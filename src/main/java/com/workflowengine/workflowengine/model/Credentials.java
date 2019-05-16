package com.workflowengine.workflowengine.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
//TODO: make it an entity and store it in the db
public class Credentials implements Serializable {

    private Integer id;
    private String username;

    private String password;


    public Credentials() {
    }

}