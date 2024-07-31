package com.edufocus.edufocus.quiz.entity;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyQuizSetResponse {

    private Long quizSetId;

    private String title;
}
