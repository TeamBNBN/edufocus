package com.edufocus.edufocus.registration.entity;

import com.edufocus.edufocus.lecture.entity.Lecture;
import com.edufocus.edufocus.user.model.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @Enumerated(EnumType.STRING)
    private RegistrationStatus status;
}
