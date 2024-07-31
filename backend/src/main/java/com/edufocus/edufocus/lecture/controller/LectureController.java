package com.edufocus.edufocus.lecture.controller;

import com.edufocus.edufocus.lecture.entity.Lecture;
import com.edufocus.edufocus.lecture.entity.LectureCreateRequest;
import com.edufocus.edufocus.lecture.entity.LectureSearchResponse;
import com.edufocus.edufocus.lecture.entity.LectureDetailResponse;
import com.edufocus.edufocus.lecture.service.LectureService;
import com.edufocus.edufocus.user.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/lecture")
@Slf4j
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;
    private final JWTUtil jwtUtil;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createLecture(@RequestHeader("Authorization") String accessToken, @RequestPart LectureCreateRequest lectureCreateRequest
            , @RequestPart(value = "image", required = false) MultipartFile image) throws Exception {
        Long userId = Long.parseLong(jwtUtil.getUserId(accessToken));

        Lecture lecture = lectureService.findLectureByTitle(lectureCreateRequest.getTitle());
        if (lecture != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        lectureService.createLecture(userId, lectureCreateRequest, image);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{lectureId}")
    public ResponseEntity<?> updateLecture(@RequestHeader("Authorization") String accessToken, @PathVariable Long lectureId, @RequestBody LectureCreateRequest lectureCreateRequest) {
        Long userId = Long.parseLong(jwtUtil.getUserId(accessToken));

        if (!lectureService.updateLecture(userId, lectureId, lectureCreateRequest)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @DeleteMapping("/{lectureId}")
    public ResponseEntity<?> deleteLecture(@RequestHeader("Authorization") String accessToken, @PathVariable long lectureId) {
        Long userId = Long.parseLong(jwtUtil.getUserId(accessToken));

        if (!lectureService.deleteLecture(userId, lectureId)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<?> findAllLecture() {
        List<LectureSearchResponse> lectures = lectureService.findAllLecture();

        return new ResponseEntity<>(lectures, HttpStatus.OK);
    }

    @GetMapping("/{lectureId}")
    public ResponseEntity<?> findById(@RequestHeader(value = "Authorization", required = false) String accessToken, @PathVariable long lectureId) {
        Long userId = null;

        if (accessToken != null) {
            userId = Long.parseLong(jwtUtil.getUserId(accessToken));
        }
        LectureDetailResponse lectureDetailResponse = lectureService.findLectureById(userId, lectureId);

        return new ResponseEntity<>(lectureDetailResponse, HttpStatus.OK);
    }

    @GetMapping("/mylecture")
    public ResponseEntity<?> findMyLecture(@RequestHeader(value = "Authorization", required = false) String accessToken) {
        if (accessToken == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        Long userId = Long.parseLong(jwtUtil.getUserId(accessToken));

        List<LectureSearchResponse> myLectures = lectureService.findMyLecture(userId);

        return new ResponseEntity<>(myLectures, HttpStatus.OK);
    }
}
