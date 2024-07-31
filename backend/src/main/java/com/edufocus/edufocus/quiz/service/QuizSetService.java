package com.edufocus.edufocus.quiz.service;

import com.edufocus.edufocus.quiz.entity.MyQuizSetResponse;
import com.edufocus.edufocus.quiz.entity.QuizSet;
import com.edufocus.edufocus.quiz.entity.QuizSetResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuizSetService {

    QuizSet createQuizSet(Long userId, String title);

    void updateQuizSet(QuizSet quizSet);

    boolean deleteQuizSet(Long userId, Long quizSetId);

    QuizSet findQuizSet(Long quizSetId);

    QuizSetResponse findQuizSetResponse(Long quizSetId);

    List<MyQuizSetResponse> findMyQuizSetResponses(Long userId);
}
