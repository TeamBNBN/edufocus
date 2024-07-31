package com.edufocus.edufocus.registration.controller;

import com.edufocus.edufocus.lecture.service.LectureService;
import com.edufocus.edufocus.registration.service.RegistrationService;
import com.edufocus.edufocus.user.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/registration")
@Slf4j
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    private final LectureService lectureService;

    private final JWTUtil jwtUtil;

    @PostMapping
    public ResponseEntity<?> register(@RequestHeader("Authorization") String accessToken, @RequestBody Map<String, Long> map) {
        Long userId = Long.parseLong(jwtUtil.getUserId(accessToken));
        Long lectureId = map.get("lectureId");

        if (!registrationService.createRegistration(userId, lectureId)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{registrationId}")
    public ResponseEntity<?> acceptRigistration(@RequestHeader("Authorization") String accessToken, @PathVariable long registrationId) {
        Long userId = Long.parseLong(jwtUtil.getUserId(accessToken));

        if (!registrationService.acceptRegistration(userId, registrationId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{registrationId}")
    public ResponseEntity<?> deleteRigistration(@RequestHeader("Authorization") String accessToken, @PathVariable long registrationId) {
        Long userId = Long.parseLong(jwtUtil.getUserId(accessToken));

        if (!registrationService.deleteRegistration(userId, registrationId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{lectureId}")
    public ResponseEntity<?> getRegistrations(@RequestHeader("Authorization") String accessToken, @PathVariable long lectureId) {
        Long userId = Long.parseLong(jwtUtil.getUserId(accessToken));

        if (!lectureService.checkTeacher(userId, lectureId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(registrationService.searchRegistrations(lectureId), HttpStatus.OK);
    }

}
