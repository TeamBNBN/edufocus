package com.edufocus.edufocus.quiz.entity;

import com.edufocus.edufocus.user.model.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class QuizSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Column
    private String title;

    @OneToMany(mappedBy = "quizSet", orphanRemoval = true)
    @JsonManagedReference
    private List<Quiz> quizzes = new ArrayList<Quiz>();

    public void addQuiz(Quiz quiz) {
        this.quizzes.add(quiz);

        if (quiz.getQuizSet() != this) {
            quiz.setQuizSet(this);
        }
    }
}
