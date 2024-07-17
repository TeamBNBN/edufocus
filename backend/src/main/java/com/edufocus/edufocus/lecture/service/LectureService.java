package com.edufocus.edufocus.lecture.service;

import com.edufocus.edufocus.lecture.entity.Lecture;
import com.edufocus.edufocus.lecture.entity.LectureRegist;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface LectureService {

    void createLecture(long userId, LectureRegist lectureRegist);

    boolean deleteLecture(long userId, long LectureId);

    List<Lecture> findAllLecture();

    Lecture findLectureByTitle(String title);

}
