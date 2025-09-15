package com.webserver.evrentalsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum Permission {
    CREATE_NEW_USER_ACCOUNT("create_new_user_account", "Tạo mới tài khoản người dùng"),
    CHANGE_USER_PASSWORD("change_user_password", "Thay đổi mật khẩu người dùng"),
    BLOCK_USER("block_user", "Khóa tài khoản người dùng"),
    //UPDATE_USER_INFO("update_user_info", "Cập nhật thông tin người dùng"),
    //CREATE_NEW_DEBT("create_new_debt", "Tạo mới khoản nợ"),
    //CONFIRM_DEBT_PAYMENT("confirm_debt_payment", "Xác nhận thanh toán khoản nợ"),
    //DELETE_DEBT("delete_debt", "Xóa khoản nợ"),
    //RESTORE_DEBT("restore_debt", "Khôi phục khoản nợ"),
    //UPDATE_DEBT("update_debt", "Cập nhật khoản nợ"),
    APPROVE_TOPUP_REQUEST("approve_topup_request", "Duyệt yêu cầu nạp tiền"),
    REJECT_TOPUP_REQUEST("reject_topup_request", "Từ chối yêu cầu nạp tiền"),
    UPDATE_PLAYED_TIME("update_played_time", "Cập nhật thời gian chơi"),
    UPDATE_FREE_ACCOUNT("update_free_account", "Cập nhật tài khoản miễn phí");

    private String key;
    private String description;

    public static Permission fromKey(String key) {
        for (Permission permission : Permission.values()) {
            if (permission.key.equals(key)) {
                return permission;
            }
        }
        return null;
    }
}
