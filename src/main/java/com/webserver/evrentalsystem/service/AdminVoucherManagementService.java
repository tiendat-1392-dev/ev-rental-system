package com.webserver.evrentalsystem.service;

import com.webserver.evrentalsystem.model.dto.CreateNewVoucherRequest;
import com.webserver.evrentalsystem.model.dto.table.PagingResponse;
import com.webserver.evrentalsystem.model.dto.table.VoucherDataItem;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public interface AdminVoucherManagementService {
    void createNewVoucher(CreateNewVoucherRequest requestBody, HttpServletRequest httpRequest);
    PagingResponse<VoucherDataItem> getVouchers(Integer pageNo, Integer pageSize, HttpServletRequest httpRequest);
}
