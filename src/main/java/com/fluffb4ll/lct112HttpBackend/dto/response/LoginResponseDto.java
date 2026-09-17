package com.fluffb4ll.lct112HttpBackend.dto.response;

import com.fluffb4ll.lct112HttpBackend.entity.DepartmentEntity;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record LoginResponseDto (
        UUID token,
        OffsetDateTime expiresAt,
        UserInfoDto user
) {
    public record UserInfoDto(
            UUID id,
            String username,
            String fullName,
            String role,
            Set<String> permissions,
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
