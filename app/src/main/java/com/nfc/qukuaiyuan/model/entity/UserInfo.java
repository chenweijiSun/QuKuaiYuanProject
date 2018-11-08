package com.nfc.qukuaiyuan.model.entity;


import java.io.Serializable;
import java.math.BigDecimal;

public class UserInfo implements Serializable {


    private Integer id;
    private String account;
    private String token;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
