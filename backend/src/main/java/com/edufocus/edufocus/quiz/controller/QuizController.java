package com.edufocus.edufocus.quiz.controller;

import com.edufocus.edufocus.quiz.entity.*;
import com.edufocus.edufocus.quiz.service.QuizService;
import com.edufocus.edufocus.quiz.service.QuizSetService;
import com.edufocus.edufocus.user.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/quiz")
@Slf4j
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    private final QuizSetService quizSetService;

    private final JWTUtil jwtUtil;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createQuizSet(@RequestHeader("Authorization") String accessToken, @RequestPart QuizSetCreateRequest quizSetCreateRequest
            , @RequestPart(value = "images", required = false) List<MultipartFile> images) throws IOException {
        Long userId = Long.parseLong(jwtUtil.getUserId(accessToken));

        QuizSet quizSet = quizSetService.createQuizSet(userId, quizSetCreateRequest.getTitle());

        int imageIdx = 0;
        for (QuizCreateRequest quizCreateRequest : quizSetCreateRequest.getQuizzes()) {
            quizService.createQuiz(quizSet, quizCreateRequest, images.get(imageIdx++));
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{quizsetId}")
    public ResponseEntity<?> getQuizzes(@PathVariable Long quizsetId) {
        QuizSetResponse quizSet = quizSetService.findQuizSetResponse(quizsetId);

        if (quizSet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(quizSet, HttpStatus.OK);
    }

    @DeleteMapping("/{quizsetId}")
    public ResponseEntity<?> deleteQuizSet(@RequestHeader("Authorization") String accessToken, @PathVariable Long quizsetId) {
        Long userId = Long.parseLong(jwtUtil.getUserId(accessToken));

        if (!quizSetService.deleteQuizSet(userId, quizsetId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<?> getQuizSets(@RequestHeader("Authorization") String accessToken) {
        Long userId = Long.parseLong(jwtUtil.getUserId(accessToken));

        return new ResponseEntity<>(quizSetService.findMyQuizSetResponses(userId), HttpStatus.OK);
    }
}
