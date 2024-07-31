package com.edufocus.edufocus.video.service;

import com.edufocus.edufocus.lecture.entity.Lecture;
import com.edufocus.edufocus.lecture.service.LectureService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
@Service
@Transactional
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoSertvice{

   private final LectureService lectureService;
    @Override
    public void startOnline(Long userId,Long lectureId) throws SQLException {

        lectureService.changeState(lectureId);
    }
}
