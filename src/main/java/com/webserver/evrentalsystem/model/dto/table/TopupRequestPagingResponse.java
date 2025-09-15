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
public class TopupRequestPagingResponse {
    private List<TopupRequestGroupItem> data;
    private int totalPages;
    private long totalElements;
    private int currentPage;
}
