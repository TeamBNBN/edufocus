package com.edufocus.edufocus.quiz.repository;

import com.edufocus.edufocus.quiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

}
