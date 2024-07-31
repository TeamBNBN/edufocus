package com.edufocus.edufocus.qna.repository;

import com.edufocus.edufocus.qna.entity.Qna;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QnaRepository extends JpaRepository<Qna, Long> {

    List<Qna> findByLectureId(Long lecturerId);
    Page<Qna> findByLectureId(Long lectureId, Pageable pageable);
}
