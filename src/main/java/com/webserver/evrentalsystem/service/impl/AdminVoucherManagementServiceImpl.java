package com.webserver.evrentalsystem.service.impl;

import com.webserver.evrentalsystem.entity.User;
import com.webserver.evrentalsystem.entity.Voucher;
import com.webserver.evrentalsystem.exception.InvalidateParamsException;
import com.webserver.evrentalsystem.model.dto.CreateNewVoucherRequest;
import com.webserver.evrentalsystem.model.dto.table.HeaderItem;
import com.webserver.evrentalsystem.model.dto.table.PagingResponse;
import com.webserver.evrentalsystem.model.dto.table.VoucherDataItem;
import com.webserver.evrentalsystem.model.mapping.VoucherMapping;
import com.webserver.evrentalsystem.repository.UserRepository;
import com.webserver.evrentalsystem.repository.VoucherRepository;
import com.webserver.evrentalsystem.service.AdminVoucherManagementService;
import com.webserver.evrentalsystem.service.validation.ManagerValidation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AdminVoucherManagementServiceImpl implements AdminVoucherManagementService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VoucherRepository voucherRepository;

    @Override
    public void createNewVoucher(CreateNewVoucherRequest requestBody, HttpServletRequest httpRequest) {

        User manager = ManagerValidation.validateAdmin(userRepository, httpRequest);

        if (!requestBody.isValid()) {
            throw new InvalidateParamsException("Invalid params");
        }

        // create new voucher
        Voucher voucher = new Voucher();
        voucher.setName(requestBody.getVoucherName());
        if (requestBody.getDescription() != null && !requestBody.getDescription().isEmpty()) {
            voucher.setDescription(requestBody.getDescription());
        }
        voucher.setImageUrl(requestBody.getImageURL());
        voucher.setMaxDiscount(requestBody.getMaxDiscount());
        voucher.setDiscountFactor(requestBody.getDiscountFactor());
        voucher.setMinTopup(requestBody.getMinTopupAmount());
        voucher.setExpiredAt(requestBody.getExpiredDate());
        voucher.setIsActive(true);
        voucher.setIsDeleted(false);
        voucher.setCreatedAt(System.currentTimeMillis());
        voucher.setCreatedBy(manager.getUserName());

        voucherRepository.save(voucher);
    }

    @Override
    public PagingResponse<VoucherDataItem> getVouchers(Integer pageNo, Integer pageSize, HttpServletRequest httpRequest) {

        if (pageNo < 1 || pageSize < 1) {
            throw new InvalidateParamsException("Truy vấn không hợp lệ!");
        }

        ManagerValidation.validateAdmin(userRepository, httpRequest);

        Page<Voucher> page = voucherRepository.findAllThatNotDeleted(PageRequest.of(Math.max((pageNo - 1), 0), pageSize));

        PagingResponse<VoucherDataItem> data = new PagingResponse<>();

        data.setCurrentPage(pageNo);
        data.setTotalPages(page.getTotalPages());
        data.setTotalElements(page.getTotalElements());

        List<HeaderItem> headerItemList = new ArrayList<>();
        headerItemList.add(new HeaderItem("Id", false));
        headerItemList.add(new HeaderItem("Tên", false));
        headerItemList.add(new HeaderItem("Giảm tối đa", false));
        headerItemList.add(new HeaderItem("Hệ số", false));
        headerItemList.add(new HeaderItem("Nạp tối thiểu", false));
        headerItemList.add(new HeaderItem("Ngày hết hạn", false));
        headerItemList.add(new HeaderItem("Đang hoạt động", false));
        headerItemList.add(new HeaderItem("Có thể sử dụng", false));
        headerItemList.add(new HeaderItem("Mô tả", false));
        headerItemList.add(new HeaderItem("Hình ảnh", false));
        headerItemList.add(new HeaderItem("Ngày tạo", false));

        data.setHeader(headerItemList);

        List<VoucherDataItem> voucherDataItems = new ArrayList<>();
        for (Voucher voucher : page.getContent()) {
            voucherDataItems.add(VoucherMapping.toVoucherDataItem(voucher));
        }

        data.setRows(voucherDataItems);
        data.setSearchTerm("");

        return data;
    }
}
