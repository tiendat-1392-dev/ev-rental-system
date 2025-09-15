package com.webserver.evrentalsystem.service.impl;

import com.webserver.evrentalsystem.entity.FreeAccount;
import com.webserver.evrentalsystem.model.dto.table.FreeAccountItemForGuest;
import com.webserver.evrentalsystem.model.mapping.FreeAccountMapping;
import com.webserver.evrentalsystem.repository.FreeAccountRepository;
import com.webserver.evrentalsystem.service.PublicService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PublicServiceImpl implements PublicService {

    @Autowired
    private FreeAccountRepository freeAccountRepository;

    @Override
    public List<FreeAccountItemForGuest> getFreeAccount(HttpServletRequest httpRequest) {

        long startDate = System.currentTimeMillis();
        // end date is next 7 days
        long endDate = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000;

        List<FreeAccount> freeAccountList = freeAccountRepository.findByDateBetween(startDate, endDate);

        List<FreeAccountItemForGuest> data = new ArrayList<>();

        for (FreeAccount freeAccount : freeAccountList) {
            data.add(FreeAccountMapping.toFreeAccountItemForGuest(freeAccount));
        }

        return data;
    }
}
