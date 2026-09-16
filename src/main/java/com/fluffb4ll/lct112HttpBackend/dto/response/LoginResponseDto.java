package com.fluffb4ll.lct112HttpBackend.dto.response;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record LoginResponseDto (
        String errorMessage,
        UUID token,
        OffsetDateTime expiresAt,
        UserInfoDto user
) {
    public record UserInfoDto(
            UUID id,
            String username,
            String fullName,
            String role,
            Map<String, Boolean> rights,
            DepartmentDto department,
            List<StudyGroupDto> studyGroups
    ) {}

    public record DepartmentDto(
            UUID id,
            String code,
            String name
    ) {}

    public record StudyGroupDto(
            UUID id,
            String name,
            UUID teacherId
    ) {}
}
