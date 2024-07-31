package com.edufocus.edufocus.quiz.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Choice {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "QuizId")
    @JsonBackReference
    private Quiz quiz;

    @Column
    private int num;

    @Column
    private String content;
}
