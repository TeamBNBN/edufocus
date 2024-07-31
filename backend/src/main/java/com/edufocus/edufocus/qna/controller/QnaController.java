package com.edufocus.edufocus.qna.controller;

import com.edufocus.edufocus.qna.entity.Qna;
import com.edufocus.edufocus.qna.entity.QnaRequestDto;
import com.edufocus.edufocus.qna.entity.QnaResponseDto;
import com.edufocus.edufocus.qna.service.QnaService;
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
    private static int PAGE_SIZE=10;


    @PostMapping("/{lecture_id}")
    public ResponseEntity<QnaResponseDto> createQna(@PathVariable("lecture_id") Long lecture_id, @RequestBody QnaRequestDto qnaRequestDto , HttpServletRequest request) {


        try{
            String token = request.getHeader("Authorization");
            Long userId = Long.parseLong(jwtUtil.getUserId(token));

           QnaResponseDto qnaResponseDto= qnaService.createQna(userId,qnaRequestDto,lecture_id);
            return new ResponseEntity<>( qnaResponseDto,HttpStatus.CREATED);

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @PostMapping({"/answer/create/{qna_id}"})
    public ResponseEntity<QnaResponseDto> createAnswer(@PathVariable("qna_id") Long qna_id, @RequestBody QnaRequestDto qnaRequestDto)
    {
        try {
            QnaResponseDto responseDto = qnaService.createAnswer(qna_id,qnaRequestDto);
            return new ResponseEntity<>(responseDto,HttpStatus.ACCEPTED);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @PutMapping({"/answer/update/{qna_id}"})
    public ResponseEntity<QnaResponseDto> updateAnswer(@PathVariable("qna_id") Long qna_id, @RequestBody QnaRequestDto qnaRequestDto)
    {
        try {
            QnaResponseDto responseDto = qnaService.updateAnswer(qna_id,qnaRequestDto);
            return new ResponseEntity<>(responseDto,HttpStatus.ACCEPTED);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/answer/delete/{qna_id}")
    public ResponseEntity<QnaResponseDto> deleteAnswer(@PathVariable("qna_id") Long qna_id)
    {
        try {
             qnaService.deleteAnswer(qna_id);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<QnaResponseDto> updateQna(@PathVariable Long id, @RequestBody QnaRequestDto qnaRequestDto) {

        try{
           QnaResponseDto qnaResponseDto=  qnaService.updateQna(id,qnaRequestDto);
            return new ResponseEntity<>(qnaResponseDto, HttpStatus.ACCEPTED);

        }catch (Exception e)
        {
            throw new RuntimeException(e);        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Qna> deleteQna(@PathVariable Long id) {
        try {
            qnaService.deleteQna(id);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<QnaResponseDto> getQna(@PathVariable Long id) {
        try{
            QnaResponseDto findQna= qnaService.getQna(id);
            return new ResponseEntity<>(findQna, HttpStatus.ACCEPTED);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/all/{id}")
    public ResponseEntity<List<QnaResponseDto>> getAllQna(@PathVariable Long id) {
        try {


            List<QnaResponseDto> qnaList= qnaService.getAllQnasByLecture(id,PAGE_SIZE);

            return new ResponseEntity<>(qnaList, HttpStatus.ACCEPTED);
        } catch (SQLException e) {
            throw new RuntimeException(e);

        }
    }
}
