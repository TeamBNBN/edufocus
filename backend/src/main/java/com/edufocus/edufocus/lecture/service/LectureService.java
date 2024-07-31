package com.edufocus.edufocus.lecture.service;

import com.edufocus.edufocus.lecture.entity.Lecture;
import com.edufocus.edufocus.lecture.entity.LectureCreateRequest;
import com.edufocus.edufocus.lecture.entity.LectureSearchResponse;
import com.edufocus.edufocus.lecture.entity.LectureDetailResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
public interface LectureService {

    void createLecture(long userId, LectureCreateRequest lectureCreateRequest, MultipartFile image) throws Exception;

    boolean updateLecture(long userId, long lectureId, LectureCreateRequest lectureCreateRequest);

    boolean deleteLecture(long userId, long LectureId);

    List<LectureSearchResponse> findAllLecture();

    LectureDetailResponse findLectureById(Long userId, long lectureId);

    List<LectureSearchResponse> findMyLecture(long userId);

    Lecture findLectureByTitle(String title);

    void changeState(Long lectureId);

    boolean getState(Long lectureId);

    boolean checkTeacher(Long userId, Long lectureId);
}
