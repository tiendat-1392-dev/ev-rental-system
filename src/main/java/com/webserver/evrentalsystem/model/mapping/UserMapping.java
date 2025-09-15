package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.Debt;
import com.webserver.evrentalsystem.entity.User;
import com.webserver.evrentalsystem.model.dto.UserDto;
import com.webserver.evrentalsystem.model.dto.table.ContentItem;
import com.webserver.evrentalsystem.model.dto.table.UserDataItem;
import com.webserver.evrentalsystem.repository.DebtRepository;
import com.webserver.evrentalsystem.utils.Formatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapping {

    @Autowired
    private DebtRepository debtRepository;

    public UserDto toUserDto(User user) {

        int amountOwed = 0;

        List<Debt> debts = debtRepository.findAllUnpaidDebtsByDebtor(user);

        int size = debts.size();
        int i;
        for (i = 0; i < size; i++) {
            amountOwed += debts.get(i).getAmountOwed();
        }

        UserDto userDto = new UserDto();

        userDto.setUserId(user.getId());
        userDto.setUserName(user.getUserName());
        userDto.setUserPublicName(user.getUserPublicName());
        userDto.setAvatar(user.getAvatar());
        userDto.setRole(user.getRole().getValue());
        userDto.setMembershipClass(user.getMembershipClass().getValue());
        userDto.setAmount(user.getAmount());
        userDto.setLuckyWheelSpin(user.getLuckyWheelSpin());
        userDto.setRealName(user.getRealName());
        userDto.setCitizenIdentityCard(user.getCitizenIdentityCard());
        userDto.setDateOfBirth(user.getDateOfBirth());
        if (user.getGender() != null) {
            userDto.setGender(user.getGender().getValue());
        }
        userDto.setAddress(user.getAddress());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setEmail(user.getEmail());
        userDto.setAmountOwed(amountOwed);
        userDto.setCreatedDate(user.getCreatedAt());

        return userDto;
    }

    public static UserDataItem toUserDataItem(User user, long disabledSessionId) {

        UserDataItem userDataItem = new UserDataItem();
        userDataItem.setUserId(new ContentItem<>(String.valueOf(user.getId()), user.getId()));
        userDataItem.setUserName(new ContentItem<>(user.getUserName(), user.getUserName()));
        userDataItem.setUserPublicName(
            new ContentItem<>(
                user.getUserPublicName() != null ? user.getUserPublicName() : "Chưa có",
                user.getUserPublicName() != null ? user.getUserPublicName() : "Chưa có"
            )
        );
        userDataItem.setAmount(new ContentItem<>(Formatter.formatCurrency(user.getAmount()), user.getAmount()));
        userDataItem.setDisabledSessionId(new ContentItem<>(String.valueOf(disabledSessionId), disabledSessionId));
        userDataItem.setStatus(
            new ContentItem<>(
                disabledSessionId == -1 ? "Hoạt động" : "Bị khóa",
                disabledSessionId == -1 ? "Hoạt động" : "Bị khóa"
            )
        );
        userDataItem.setCreatedDate(new ContentItem<>(Formatter.formatTimestampToDateTimeString(user.getCreatedAt()), user.getCreatedAt()));

        return userDataItem;
    }
}
