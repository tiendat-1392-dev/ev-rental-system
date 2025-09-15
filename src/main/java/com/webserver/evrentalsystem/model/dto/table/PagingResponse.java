package com.webserver.evrentalsystem.model.dto.table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PagingResponse<T> {
    private List<HeaderItem> header;
    private List<T> rows;
    private int totalPages;
    private long totalElements;
    private int currentPage;
    private String searchTerm;
}
