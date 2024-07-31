package com.edufocus.edufocus.lecture.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LectureCreateRequest {

    private String title;

    private String description;

    private String plan;

    private Date startDate;

    private Date endDate;

    private String time;

}
