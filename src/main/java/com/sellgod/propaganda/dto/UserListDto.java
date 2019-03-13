package com.sellgod.propaganda.dto;

import lombok.Data;

/**
 * @author 姜文超
 * @date 2019/3/8 11:59
 * @description
 */
@Data
public class UserListDto {

    private String name;

    private String img;

    private String info;

    public String getName() {
        return name;
    }

    public String getImg() {
        return img;
    }

    public String getInfo() {
        return info;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
