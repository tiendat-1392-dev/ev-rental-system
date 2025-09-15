package com.webserver.evrentalsystem.model.dto.table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PlayedTimeItem {
    private ContentItem<String> computerName;
    private ContentItem<String> userName;
    private ContentItem<Long> date;
    private ContentItem<Long> time;
    private ContentItem<Integer> playedTime;
}
