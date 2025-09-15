package com.webserver.evrentalsystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateNewVoucherRequest {
    private String voucherName;
    private String description;
    private String imageURL;
    private Integer maxDiscount;
    private Double discountFactor;
    private Integer minTopupAmount;
    private Long expiredDate;

    public boolean isValid() {
        return
                voucherName != null &&
                description != null &&
                imageURL != null &&
                maxDiscount != null &&
                discountFactor != null &&
                minTopupAmount != null &&
                expiredDate != null &&
                !voucherName.isEmpty() &&
                !imageURL.isEmpty() &&
                maxDiscount >= 0 &&
                discountFactor > 1 &&
                minTopupAmount >= 0 &&
                expiredDate > System.currentTimeMillis();
    }
}
