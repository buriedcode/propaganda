package com.sellgod.propaganda.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@TableName("user_account")
public class UserEntity {

    @TableId
    private long id;

    private String userName;

    private String phone;

    private long picId;

    private String info;

    public long getId() {
        return id;
    }

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

    public void setId(long id) {
        this.id = id;
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
