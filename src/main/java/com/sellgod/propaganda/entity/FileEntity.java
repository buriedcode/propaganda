package com.sellgod.propaganda.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@Data
@TableName("file_pic")
public class FileEntity {

    private long id;

    private String url;

    public long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
