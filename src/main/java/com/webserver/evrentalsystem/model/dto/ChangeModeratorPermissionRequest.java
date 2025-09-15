package com.webserver.evrentalsystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeModeratorPermissionRequest {
    private Long userId;
    private List<ModeratorPermissionDto> permissions;
}
