package com.webserver.evrentalsystem.model.dto;

import com.webserver.evrentalsystem.utils.Constant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BlockUserRequest {
    private Long userId;
    private String reason;
    private Long unblockTime;

    public boolean isParamsValid() {
        return userId != null && reason != null && unblockTime != null && !reason.isEmpty();
    }

    public boolean isUnblockTimeValid() {
        return unblockTime - System.currentTimeMillis() >= Constant.MINIMUM_BLOCK_TIME_IN_MILLISECONDS;
    }

    public boolean isReasonValid() {
        return reason.length() <= Constant.MAXIMUM_REASONS_LENGTH;
    }
}
