package com.edufocus.edufocus.quiz.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "quizset_id")
    @JsonBackReference
    private QuizSet quizSet;

    @Column
    private String question;

    @Column
    private String image;

    @Column
    private String answer;

    @OneToMany(mappedBy = "quiz", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Choice> choices;

    public void setQuizSet(QuizSet quizSet) {
        this.quizSet = quizSet;

        if (!quizSet.getQuizzes().contains(this)) {
            quizSet.getQuizzes().remove(this);
        }
    }

    public void addChoice(Choice choice) {
        this.choices.add(choice);

        if (choice.getQuiz() != this) {
            choice.setQuiz(this);
        }
    }
}
