package com.edufocus.edufocus.board.entity.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
@Getter
@Setter
public class ResponseCommentDto {
    private long id;
    private String name;
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;
}
