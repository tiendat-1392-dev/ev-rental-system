package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.Debt;
import com.webserver.evrentalsystem.model.dto.table.ContentItem;
import com.webserver.evrentalsystem.model.dto.table.DebtDataItem;
import com.webserver.evrentalsystem.model.dto.table.DebtInfoItem;
import com.webserver.evrentalsystem.utils.Formatter;

public class DebtMapping {
    public static DebtDataItem toDebtDataItem(Debt debt) {
        DebtDataItem debtDataItem = new DebtDataItem();
        debtDataItem.setDebtId(debt.getId());
        debtDataItem.setCreditorUsername(debt.getCreditor().getUserName());
        debtDataItem.setAmountOwed(debt.getAmountOwed());
        debtDataItem.setOwedDate(Formatter.formatTimestampToDateTimeString(debt.getOwedDate()));
        debtDataItem.setDescription(debt.getDescription());

        return debtDataItem;
    }

    public static DebtInfoItem toDebtInfoItem(Debt debt) {
        DebtInfoItem debtInfoItem = new DebtInfoItem();
        debtInfoItem.setDebtId(new ContentItem<>(String.valueOf(debt.getId()), debt.getId()));
        debtInfoItem.setDebtorUserName(new ContentItem<>(debt.getDebtor().getUserName(), debt.getDebtor().getUserName()));
        debtInfoItem.setOwedDate(new ContentItem<>(Formatter.formatTimestampToDateTimeString(debt.getOwedDate()), debt.getOwedDate()));
        debtInfoItem.setCreatorUserName(new ContentItem<>(debt.getCreditor().getUserName(), debt.getCreditor().getUserName()));
        debtInfoItem.setCreatedDate(new ContentItem<>(Formatter.formatTimestampToDateTimeString(debt.getCreatedAt()), debt.getCreatedAt()));
        if (debt.getConfirmedPaymentBy() != null) {
            debtInfoItem.setConfirmedByUserName(new ContentItem<>(debt.getConfirmedPaymentBy().getUserName(), debt.getConfirmedPaymentBy().getUserName()));
            debtInfoItem.setConfirmedDate(new ContentItem<>(Formatter.formatTimestampToDateTimeString(debt.getPaidDate()), debt.getPaidDate()));
        } else {
            debtInfoItem.setConfirmedByUserName(new ContentItem<>("Chưa rõ", "Chưa rõ"));
            debtInfoItem.setConfirmedDate(new ContentItem<>("0", 0L));
        }
        debtInfoItem.setAmountOwed(new ContentItem<>(Formatter.formatCurrency(debt.getAmountOwed()), debt.getAmountOwed()));
        debtInfoItem.setDescription(new ContentItem<>(debt.getDescription(), debt.getDescription()));

        return debtInfoItem;
    }
}
