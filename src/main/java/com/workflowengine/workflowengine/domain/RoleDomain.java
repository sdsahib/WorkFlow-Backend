package com.workflowengine.workflowengine.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDomain {
    private Integer id;
    private String name;

    public RoleDomain() {
    }

    public RoleDomain(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
