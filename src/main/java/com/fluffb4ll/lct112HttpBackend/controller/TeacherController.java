package com.fluffb4ll.lct112HttpBackend.controller;

import com.fluffb4ll.lct112HttpBackend.dto.request.CreateStudyGroupRequestDto;
import com.fluffb4ll.lct112HttpBackend.dto.request.UpdateStudyGroupRequestDto;
import com.fluffb4ll.lct112HttpBackend.dto.response.*;
import com.fluffb4ll.lct112HttpBackend.service.StudyGroupUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final StudyGroupUpdateService studyGroupUpdateService;

    @PostMapping("/studyGroup/create")
    public ResponseEntity<CreateStudyGroupResponseDto> createStudyGroup(
            @CookieValue(name = "AUTH_TOKEN") String token,
            @RequestBody CreateStudyGroupRequestDto request
    ) throws AuthenticationException {
        UUID newGroupId = studyGroupUpdateService.createStudyGroup(UUID.fromString(token), request);
        URI location = URI.create("/api/teacher/studyGroup/" + newGroupId);
        return ResponseEntity.created(location).body(new CreateStudyGroupResponseDto(newGroupId));
    }

    @PostMapping("/studyGroup/update")
    public ResponseEntity<Void> updateStudyGroup(
            @CookieValue(name = "AUTH_TOKEN") String token,
            @RequestBody UpdateStudyGroupRequestDto request
    ) throws AuthenticationException {
        studyGroupUpdateService.updateStudyGroup(UUID.fromString(token), request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/studyGroup/{uuid}")
    public ResponseEntity<Void> deleteStudyGroup(
            @CookieValue(name = "AUTH_TOKEN") String token,
            @PathVariable("uuid") UUID groupId
    ) throws AuthenticationException {
        studyGroupUpdateService.deleteStudyGroup(UUID.fromString(token), groupId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/studyGroup/{uuid}")
    public ResponseEntity<StudyGroupInfoDto> getStudyGroup(
            @CookieValue(name = "AUTH_TOKEN") String token,
            @PathVariable("uuid") UUID groupId
    ) throws AuthenticationException {
        StudyGroupInfoDto response = studyGroupUpdateService.getStudyGroup(UUID.fromString(token), groupId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/studyGroups")
    public ResponseEntity<PageResponseDto<StudyGroupTableRowDto>> getStudyGroups(
            @CookieValue(name = "AUTH_TOKEN") String token,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) throws AuthenticationException {
        PageResponseDto<StudyGroupTableRowDto> response = studyGroupUpdateService.getStudyGroups(
                UUID.fromString(token),
                page,
                size
        );
        return ResponseEntity.ok(response);
    }
}