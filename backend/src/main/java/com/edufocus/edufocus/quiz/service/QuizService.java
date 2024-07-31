package com.edufocus.edufocus.quiz.service;

import com.edufocus.edufocus.quiz.entity.QuizCreateRequest;
import com.edufocus.edufocus.quiz.entity.QuizSet;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface QuizService {

    void createQuiz(QuizSet quizSet, QuizCreateRequest quizCreateRequest, MultipartFile quizImage) throws IOException;

}
