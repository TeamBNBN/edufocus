package com.edufocus.edufocus.quiz.service;

import com.edufocus.edufocus.quiz.entity.*;
import com.edufocus.edufocus.quiz.repository.QuizSetRepository;
import com.edufocus.edufocus.user.model.entity.User;
import com.edufocus.edufocus.user.model.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class QuizSetServiceImpl implements QuizSetService {

    private final QuizSetRepository quizSetRepository;

    private final UserRepository userRepository;

    @Override
    public QuizSet createQuizSet(Long userId, String title) {
        QuizSet quizSet = new QuizSet();

        User user = userRepository.findById(userId).get();
        quizSet.setUser(user);
        quizSet.setTitle(title);

        return quizSetRepository.save(quizSet);
    }

    @Override
    public void updateQuizSet(QuizSet quizSet) {

    }

    @Override
    public boolean deleteQuizSet(Long userId, Long quizSetId) {
        Optional<QuizSet> quizSet = quizSetRepository.findById(quizSetId);

        if (quizSet.isEmpty() || userId != quizSet.get().getUser().getId()) {
            return false;
        }

        quizSetRepository.deleteById(quizSetId);
        return true;
    }

    @Override
    public QuizSet findQuizSet(Long quizSetId) {
        return quizSetRepository.findById(quizSetId).get();
    }

    @Override
    public QuizSetResponse findQuizSetResponse(Long quizSetId) {
        Optional<QuizSet> quizSet = quizSetRepository.findById(quizSetId);

        if (quizSet.isEmpty()) {
            return null;
        }


        List<QuizResponse> quizResponses = new ArrayList<>();
        for (Quiz quiz : quizSet.get().getQuizzes()) {
            QuizResponse quizResponse = new QuizResponse().builder()
                    .question(quiz.getQuestion())
                    .image(quiz.getImage())
                    .choices(quiz.getChoices())
                    .build();
            quizResponses.add(quizResponse);
        }

        QuizSetResponse quizSetResponse = new QuizSetResponse().builder()
                .title(quizSet.get().getTitle())
                .quizzes(quizResponses)
                .build();

        return quizSetResponse;
    }

    @Override
    public List<MyQuizSetResponse> findMyQuizSetResponses(Long userId) {
        List<QuizSet> quizSetList = quizSetRepository.findQuizSetsByUserId(userId);

        List<MyQuizSetResponse> myQuizSetResponses = new ArrayList<>();
        for (QuizSet quizSet : quizSetList) {
            MyQuizSetResponse myQuizSetResponse = new MyQuizSetResponse().builder()
                    .quizSetId(quizSet.getId())
                    .title(quizSet.getTitle())
                    .build();

            myQuizSetResponses.add(myQuizSetResponse);
        }

        return myQuizSetResponses;

    }

}
