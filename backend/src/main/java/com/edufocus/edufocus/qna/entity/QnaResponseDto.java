package com.edufocus.edufocus.qna.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Getter

@NoArgsConstructor
@AllArgsConstructor
public class QnaResponseDto {


    private Long id;
    private String title;
    private String username;
    private String content;
    private Date createtAt;
    private String answer;
    public static QnaResponseDto toEntity(Qna qna)
    {
        return new QnaResponseDto(
                qna.getId(),
                qna.getTitle(),
                qna.getUser().getName(),
                qna.getContent(),
                qna.getCreatedAt(),
        qna.getAnswer()
        );
    }


}



