package com.postech.adjt.api.payload;

import java.util.List;

import com.postech.adjt.domain.dto.filtro.FilterDTO;
import com.postech.adjt.domain.dto.filtro.SortDTO;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.Min;

public class PaginacaoPayLoad {

    @NonNull
    @Min(0)
    private int page;

    @NonNull
    @Min(5)
    private int size;

    private List<FilterDTO> filters;

    private List<SortDTO> sorts;

    public PaginacaoPayLoad() {
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<FilterDTO> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterDTO> filters) {
        this.filters = filters;
    }

    public List<SortDTO> getSorts() {
        return sorts;
    }

    public void setSorts(List<SortDTO> sorts) {
        this.sorts = sorts;
    }
}
