package com.sellgod.propaganda.dto;

import lombok.Data;

@Data
public class RegisterDto {


    private String userName;

    private String phone;

    private long picId;

    private String info;



    public String getUserName() {
        return userName;
    }

    public String getPhone() {
        return phone;
    }

    public long getPicId() {
        return picId;
    }

    public String getInfo() {
        return info;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPicId(long picId) {
        this.picId = picId;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
