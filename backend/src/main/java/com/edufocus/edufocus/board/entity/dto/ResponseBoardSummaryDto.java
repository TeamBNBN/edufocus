package com.edufocus.edufocus.board.entity.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class ResponseBoardSummaryDto {
    private long id;
    private String name;
    private String title;
    private LocalDateTime createdAt;
}
