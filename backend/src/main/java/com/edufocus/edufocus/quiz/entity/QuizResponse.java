package com.edufocus.edufocus.quiz.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizResponse {

    private String question;

    private String image;

    private List<Choice> choices;
}
