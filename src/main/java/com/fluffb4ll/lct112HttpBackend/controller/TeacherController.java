package com.fluffb4ll.lct112HttpBackend.controller;

import com.fluffb4ll.lct112HttpBackend.dto.request.CreateStudyGroupRequestDto;
import com.fluffb4ll.lct112HttpBackend.dto.request.UpdateStudyGroupRequestDto;
import com.fluffb4ll.lct112HttpBackend.dto.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherController {
    @PostMapping("/studyGroup/create")
    public ResponseEntity<CreateStudyGroupResponseDto> createStudyGroup(
            @CookieValue(name = "AUTH_TOKEN") String token,
            CreateStudyGroupRequestDto request
    ) {
        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/studyGroup/update")
    public ResponseEntity<Void> updateStudyGroup(
            @CookieValue(name = "AUTH_TOKEN") String token,
            UpdateStudyGroupRequestDto request
    ) {
        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping("/studyGroup/{uuid}")
    public ResponseEntity<Void> deleteStudyGroup(
            @CookieValue(name = "AUTH_TOKEN") String token,
            @PathVariable("uuid") UUID groupId
    ) {
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/studyGroup/{uuid}")
    public ResponseEntity<StudyGroupInfoDto> getStudyGroup(
            @CookieValue(name = "AUTH_TOKEN") String token,
            @PathVariable("uuid") UUID groupId
    ) {
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/studyGroups")
    public ResponseEntity<PageResponseDto<StudyGroupTableRowDto>> getStudyGroups(
            @CookieValue(name = "AUTH_TOKEN") String token,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        return ResponseEntity.ok().body(null);
    }
}
