package com.edufocus.edufocus.quiz.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChoiceCreateRequest {

    private int num;

    private String content;
}
