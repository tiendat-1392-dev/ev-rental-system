package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.Voucher;
import com.webserver.evrentalsystem.model.dto.table.ContentItem;
import com.webserver.evrentalsystem.model.dto.table.VoucherDataItem;
import com.webserver.evrentalsystem.utils.Formatter;

public class VoucherMapping {
    public static VoucherDataItem toVoucherDataItem(Voucher voucher) {
        VoucherDataItem voucherDataItem = new VoucherDataItem();
        voucherDataItem.setId(new ContentItem<>(voucher.getId().toString(), voucher.getId()));
        voucherDataItem.setName(new ContentItem<>(voucher.getName(), voucher.getName()));
        voucherDataItem.setMaxDiscount(new ContentItem<>(Formatter.formatCurrency(voucher.getMaxDiscount()), voucher.getMaxDiscount()));
        voucherDataItem.setDiscountFactor(new ContentItem<>(voucher.getDiscountFactor().toString(), voucher.getDiscountFactor()));
        voucherDataItem.setMinTopup(new ContentItem<>(Formatter.formatCurrency(voucher.getMinTopup()), voucher.getMinTopup()));
        voucherDataItem.setExpiredAt(new ContentItem<>(Formatter.formatTimestampToDateTimeString(voucher.getExpiredAt()), voucher.getExpiredAt()));
        voucherDataItem.setIsActive(new ContentItem<>(voucher.getIsActive() ? "Có" : "Không", voucher.getIsActive()));

        boolean isCanUse = voucher.getExpiredAt() > System.currentTimeMillis() && voucher.getIsActive();
        voucherDataItem.setCanUse(new ContentItem<>(isCanUse ? "Có" : "Không", isCanUse));

        voucherDataItem.setDescription(new ContentItem<>(voucher.getDescription() != null ? voucher.getDescription() : "", voucher.getDescription()));
        voucherDataItem.setImageUrl(new ContentItem<>(voucher.getImageUrl(), voucher.getImageUrl()));
        voucherDataItem.setCreatedAt(new ContentItem<>(Formatter.formatTimestampToDateTimeString(voucher.getCreatedAt()), voucher.getCreatedAt()));

        return voucherDataItem;
    }
}
