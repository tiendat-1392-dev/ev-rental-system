package com.webserver.evrentalsystem.controller;

import com.webserver.evrentalsystem.model.dto.CreateNewVoucherRequest;
import com.webserver.evrentalsystem.model.dto.table.PagingResponse;
import com.webserver.evrentalsystem.model.dto.table.VoucherDataItem;
import com.webserver.evrentalsystem.service.AdminVoucherManagementService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/admin/voucher")
public class AdminVoucherManagementController {

    @Autowired
    private AdminVoucherManagementService adminVoucherManagementService;

    @PostMapping(value = "/create-new-voucher")
    public void createNewVoucher(@RequestBody CreateNewVoucherRequest requestBody, HttpServletRequest httpRequest) {
        adminVoucherManagementService.createNewVoucher(requestBody, httpRequest);
    }

    @GetMapping(value = "/get-vouchers")
    public ResponseEntity<PagingResponse<VoucherDataItem>> getVouchers(
        @RequestParam(defaultValue = "1") Integer pageNo,
        @RequestParam(defaultValue = "30") Integer pageSize,
        HttpServletRequest httpRequest
    ) {
        return ResponseEntity.ok(adminVoucherManagementService.getVouchers(pageNo, pageSize, httpRequest));
    }
}
