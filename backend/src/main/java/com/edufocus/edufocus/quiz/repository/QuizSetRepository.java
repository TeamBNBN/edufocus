package com.edufocus.edufocus.quiz.repository;

import com.edufocus.edufocus.quiz.entity.QuizSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizSetRepository extends JpaRepository<QuizSet, Long> {

    List<QuizSet> findQuizSetsByUserId(Long userId);
}
