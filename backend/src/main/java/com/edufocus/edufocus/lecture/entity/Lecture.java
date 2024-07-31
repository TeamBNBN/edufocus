package com.edufocus.edufocus.lecture.entity;


import com.edufocus.edufocus.user.model.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String title;

    @Lob
    private String description;

    @Lob
    private String plan;

    @Column
    private String image;

    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Column
    private String time;

    @Column(columnDefinition = "boolean default false")
    private boolean online;

}
