package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.PlayedTime;
import com.webserver.evrentalsystem.model.dto.PlayedTimeRequestData;
import com.webserver.evrentalsystem.model.dto.table.ContentItem;
import com.webserver.evrentalsystem.model.dto.table.PlayedTimeItem;
import com.webserver.evrentalsystem.utils.Formatter;

public class PlayedTimeMapping {
    public static PlayedTimeItem toPlayedTimeItem(PlayedTime playedTime) {
        PlayedTimeItem playedTimeItem = new PlayedTimeItem();
        playedTimeItem.setComputerName(new ContentItem<>(playedTime.getComputerName(), playedTime.getComputerName()));
        playedTimeItem.setUserName(new ContentItem<>(playedTime.getUserName(), playedTime.getUserName()));
        playedTimeItem.setDate(
                new ContentItem<>(
                    Formatter.formatTimestampToDateTimeString(playedTime.getStartTime(), "dd-MMM-yyyy"),
                    playedTime.getStartTime()
                )
        );
        playedTimeItem.setTime(
                new ContentItem<>(
                    Formatter.formatTimestampToDateTimeString(playedTime.getStartTime(), "HH:mm:ss"),
                    playedTime.getStartTime()
                )
        );
        playedTimeItem.setPlayedTime(
                new ContentItem<>(
                        Formatter.formatMinutesToHourMinuteString(playedTime.getPlayedTimeInMinutes()),
                        playedTime.getPlayedTimeInMinutes()
                )
        );
        return playedTimeItem;
    }

    public static PlayedTime toPlayedTime(PlayedTimeRequestData playedTimeRequestData) {
        PlayedTime playedTime = new PlayedTime();
        playedTime.setComputerName(playedTimeRequestData.getComputerName());
        playedTime.setUserName(playedTimeRequestData.getUserName());
        playedTime.setStartTime(playedTimeRequestData.getStartTime());
        playedTime.setPlayedTimeInMinutes(playedTimeRequestData.getPlayedTime());
        return playedTime;
    }
}
