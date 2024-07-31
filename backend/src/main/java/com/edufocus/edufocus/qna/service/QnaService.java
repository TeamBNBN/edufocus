package com.edufocus.edufocus.qna.service;

import com.edufocus.edufocus.lecture.entity.Lecture;
import com.edufocus.edufocus.qna.entity.Qna;
import com.edufocus.edufocus.qna.entity.QnaRequestDto;
import com.edufocus.edufocus.qna.entity.QnaResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
@Service
public interface QnaService {

    QnaResponseDto createQna(Long id, QnaRequestDto qnaRequestDto, Long lecture_id) throws SQLException;
    QnaResponseDto updateQna(Long id,QnaRequestDto qnaRequestDto) throws SQLException;
    void deleteQna(Long id) throws SQLException;
    QnaResponseDto getQna(Long id) throws SQLException;

    List<QnaResponseDto> getAllQnasByLecture(Long lectureId,int pageNumber) throws SQLException;
    QnaResponseDto createAnswer(Long id,QnaRequestDto qnaRequestDto) throws SQLException;
    QnaResponseDto updateAnswer(Long id,QnaRequestDto qnaRequestDto) throws SQLException;
    void deleteAnswer(Long id) throws SQLException;

}
