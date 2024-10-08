package com.edufocus.edufocus.qna.controller;

import com.edufocus.edufocus.qna.entity.Qna;
import com.edufocus.edufocus.qna.entity.QnaRequestDto;
import com.edufocus.edufocus.qna.entity.QnaResponseDto;
import com.edufocus.edufocus.qna.service.QnaService;
import com.edufocus.edufocus.user.model.entity.vo.User;
import com.edufocus.edufocus.user.model.entity.vo.UserRole;
import com.edufocus.edufocus.user.model.repository.UserRepository;
import com.edufocus.edufocus.user.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/qna")
@Slf4j
@RequiredArgsConstructor
public class QnaController {
    private final QnaService qnaService;
    private final JWTUtil jwtUtil;
    private static int PAGE_SIZE = 10;
    private final UserRepository userRepository;

    @PostMapping("/{lecture_id}")
    public ResponseEntity<QnaResponseDto> createQna(@PathVariable("lecture_id") Long lecture_id, @RequestBody QnaRequestDto qnaRequestDto, HttpServletRequest request) {


        try {
            String token = request.getHeader("Authorization");
            Long userId = Long.parseLong(jwtUtil.getUserId(token));

            QnaResponseDto qnaResponseDto = qnaService.createQna(userId, qnaRequestDto, lecture_id);
            return new ResponseEntity<>(qnaResponseDto, HttpStatus.CREATED);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping({"/answer/create/{qna_id}"})
    public ResponseEntity<QnaResponseDto> createAnswer(@PathVariable("qna_id") Long qna_id, @RequestBody QnaRequestDto qnaRequestDto, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            System.out.println(token);
            Long userId = Long.parseLong(jwtUtil.getUserId(token));
            User findUser = userRepository.findById(userId).orElse(null);

            if (findUser.getRole() != UserRole.ADMIN) {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

            }

            QnaResponseDto responseDto = qnaService.createAnswer(qna_id, qnaRequestDto);
            return new ResponseEntity<>(responseDto, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

        }
    }

    @PutMapping({"/answer/update/{qna_id}"})
    public ResponseEntity<QnaResponseDto> updateAnswer(@PathVariable("qna_id") Long qna_id, @RequestBody QnaRequestDto qnaRequestDto, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            Long userId = Long.parseLong(jwtUtil.getUserId(token));
            User findUser = userRepository.findById(userId).orElse(null);

            if (findUser.getRole() != UserRole.ADMIN) {
                System.out.println("role 안맞음");
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }

            QnaResponseDto responseDto = qnaService.updateAnswer(qna_id, qnaRequestDto);
            return new ResponseEntity<>(responseDto, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping("/answer/delete/{qna_id}")
    public ResponseEntity<QnaResponseDto> deleteAnswer(@PathVariable("qna_id") Long qna_id, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            Long userId = Long.parseLong(jwtUtil.getUserId(token));
            User findUser = userRepository.findById(userId).orElse(null);

            System.out.println("delete answer");
            if (findUser.getRole() != UserRole.ADMIN) {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
            qnaService.deleteAnswer(qna_id);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<QnaResponseDto> updateQna(@PathVariable Long id, @RequestBody QnaRequestDto qnaRequestDto, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Long userId = Long.parseLong(jwtUtil.getUserId(token));
        try {
            QnaResponseDto qnaResponseDto = qnaService.updateQna(id, qnaRequestDto, userId);
            return new ResponseEntity<>(qnaResponseDto, HttpStatus.ACCEPTED);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Qna> deleteQna(@PathVariable Long id, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            Long userId = Long.parseLong(jwtUtil.getUserId(token));
            qnaService.deleteQna(id, userId);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);

        } catch (SQLException e) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<QnaResponseDto> getQna(@PathVariable Long id, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            long userId = Long.parseLong(jwtUtil.getUserId(token));
            QnaResponseDto findQna = qnaService.getQna(id, userId);
            return new ResponseEntity<>(findQna, HttpStatus.ACCEPTED);

        } catch (SQLException e) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/all/{id}")
    public ResponseEntity<List<QnaResponseDto>> getAllQna(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int pageNo) {
        try {
            List<QnaResponseDto> qnaList = qnaService.getAllQnasByLecture(id, pageNo, PAGE_SIZE);
            return new ResponseEntity<>(qnaList, HttpStatus.ACCEPTED);
        } catch (SQLException e) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
