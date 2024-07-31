package com.edufocus.edufocus.quiz.entity;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizSetResponse {

    private String title;

    private List<QuizResponse> quizzes;
}
