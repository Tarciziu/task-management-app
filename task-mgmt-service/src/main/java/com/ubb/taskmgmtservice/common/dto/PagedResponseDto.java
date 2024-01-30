package com.ubb.taskmgmtservice.common.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PagedResponseDto<T> {
    private List<T> data;
    private PagingMetadata metadata;

    @Setter
    @Getter
    public static class PagingMetadata {
        private Integer totalPages;
        private Long totalItems;
    }
}
