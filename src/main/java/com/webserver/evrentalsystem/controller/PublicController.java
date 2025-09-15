package com.webserver.evrentalsystem.controller;

import com.webserver.evrentalsystem.model.dto.table.FreeAccountItemForGuest;
import com.webserver.evrentalsystem.service.PublicService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="api/public")
public class PublicController {

    @Autowired
    private PublicService publicService;

    @GetMapping(value = "/get-free-account")
    public ResponseEntity<List<FreeAccountItemForGuest>> getFreeAccount(
            HttpServletRequest httpRequest
    ) {
        List<FreeAccountItemForGuest> data = publicService.getFreeAccount(httpRequest);
        return ResponseEntity.ok(data);
    }
}
