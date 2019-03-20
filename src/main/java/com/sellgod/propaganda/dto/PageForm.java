package com.sellgod.propaganda.dto;


import lombok.Data;

/***
 *  Created By 张猛 on 2018/6/28
 */

@Data
public class PageForm {

    private Integer page = 1;

    private Integer limit = 10;
}
