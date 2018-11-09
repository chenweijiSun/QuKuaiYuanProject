package com.nfc.qukuaiyuan.model.entity;


import java.io.Serializable;
import java.math.BigDecimal;

public class UserInfo implements Serializable {


    private static final long serialVersionUID = 1633721944009608297L;
    private String user_id;
    private String mobile;
    private String name;
    private String time;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
