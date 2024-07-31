package com.edufocus.edufocus.board.entity.vo;


import com.edufocus.edufocus.board.entity.dto.ResponseBoardDetailDto;
import com.edufocus.edufocus.board.entity.dto.ResponseBoardSummaryDto;
import com.edufocus.edufocus.lecture.entity.Lecture;
import com.edufocus.edufocus.user.model.entity.User;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@EntityListeners(AuditingEntityListener.class)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Board {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String content;

    @Column(nullable = true)
    private int viewCount;

    @CreatedDate
    LocalDateTime createdAt;

    @LastModifiedDate
    LocalDateTime modifiedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;


    @OneToMany(mappedBy = "board")
    private List<Comment> comments;

    public ResponseBoardSummaryDto makeSummaryDto(){
        return ResponseBoardSummaryDto.builder()
                .id(id)
                .title(title)
                .name(user.getUserId())
                .createdAt(createdAt)
                .build();
    }

    public ResponseBoardDetailDto makeDetailDto(){
        return ResponseBoardDetailDto.builder()
                .id(id)
                .name(user.getEmail())
                .title(title)
                .content(content)
                .viewCount(viewCount)
                .createdAt(createdAt)
                .modifiedAt(modifiedAt)
                .build();
    }
}
