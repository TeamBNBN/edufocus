package com.edufocus.edufocus.registration.entity;

import com.edufocus.edufocus.lecture.entity.Lecture;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationSearchResponse {

    private Long id;

    private String userName;
}
