package com.sellgod.propaganda.entity;

import lombok.Data;

@Data
public class Imgs {

    private String imgUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
