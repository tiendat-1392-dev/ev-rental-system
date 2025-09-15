package com.webserver.evrentalsystem.utils;

import com.webserver.evrentalsystem.entity.BlockedSession;

public class CommonUtils {
    public static String generateBlockMessage(BlockedSession blockedSession) {
        return "Tài khoản " + blockedSession.getBlockedUser().getUserName()
                + " đã bị khóa bởi " + blockedSession.getBlocker().getUserName()
                + " với lý do: " + blockedSession.getReason()
                + " vào lúc " + Formatter.formatTimestampToDateTimeString(blockedSession.getBlockTime())
                + ". Tài khoản sẽ được mở khóa vào lúc " + (blockedSession.getUnblockTime() == null ? "không xác định" : Formatter.formatTimestampToDateTimeString(blockedSession.getUnblockTime()));
    }
}
