package com.edufocus.edufocus.qna.service;

import com.edufocus.edufocus.lecture.entity.Lecture;
import com.edufocus.edufocus.qna.entity.Qna;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
@Service
public interface QnaService {

    void createQna(Qna qna) throws SQLException;
    void updateQna(Long id,Qna qna) throws SQLException;
    void deleteQna(Long id) throws SQLException;
    Qna getQna(Long id) throws SQLException;
    List<Qna> getAllQnasByLecture(Long lectureId) throws SQLException;
}
