package com.workflowengine.workflowengine.domain;

import com.workflowengine.workflowengine.model.SysUser;

import javax.persistence.*;
import java.io.Serializable;

public class RefreshToken implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RefreshTokenId")
    private long id;
    @Column(name = "RefreshToken")
    private String refreshToken;
    @JoinColumn(unique = true, name = "UserId")
    @OneToOne(cascade = CascadeType.MERGE)
    private SysUser sysUser;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public SysUser getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }
}
