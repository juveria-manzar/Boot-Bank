package com.juveriatech.demo.dto;

import org.springframework.data.domain.Page;

public class PageResponse<T> {
    private Page<T> page;

    public PageResponse(Page<T> page) {
        this.page = page;
    }

    public Page<T> getPage() {
        return page;
    }

    public void setPage(Page<T> page) {
        this.page = page;
    }
}