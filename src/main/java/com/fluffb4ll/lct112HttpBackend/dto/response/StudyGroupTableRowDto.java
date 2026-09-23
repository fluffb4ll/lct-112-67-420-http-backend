package com.fluffb4ll.lct112HttpBackend.dto.response;

import java.util.List;
import java.util.UUID;

public record StudyGroupTableRowDto(
        UUID id,
        String name,
        UserInfoDto teacher,
        List<UserInfoDto> students
) {
    public record UserInfoDto(
            UUID id,
            String fullName
    ) {}
}
