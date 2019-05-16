package com.workflowengine.workflowengine.model;

import com.workflowengine.workflowengine.domain.RefreshToken;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class SysUser implements Serializable {

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String alternatePhone;
    private String fax;
    private Date createdOn;
    private Credentials credentials;
    private RefreshToken refreshToken;
    private long createdBy;
    private long modifiedBy;
    private Date modifiedOn;


}
