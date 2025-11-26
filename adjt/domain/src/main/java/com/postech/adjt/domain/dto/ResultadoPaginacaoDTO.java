package com.postech.adjt.domain.dto;

import java.io.Serializable;
import java.util.List;

public class ResultadoPaginacaoDTO<T> implements Serializable {

    private static final long serialVersionUID = 907682988992882263L;

    private final List<T> content;
    private final int pageNumber;
    private final int pageSize;
    private final long totalElements;

    public ResultadoPaginacaoDTO(List<T> content, int pageNumber, int pageSize, long totalElements) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
    }

    public List<T> getContent() {
        return content;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getTotalElements() {
        return totalElements;
    }

    
}
