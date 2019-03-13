package com.sellgod.propaganda.dto;

import lombok.Data;

@Data
public class PageDto {

    /**
     * 每页数量
     */
    private int pageSize;

    /**
     * 第几页
     */
    private int currentPage;

    public int getPageSize() {
        return pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
