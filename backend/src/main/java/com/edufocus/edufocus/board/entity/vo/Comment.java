package com.edufocus.edufocus.board.entity.vo;


import com.edufocus.edufocus.board.entity.dto.ResponseCommentDto;
import com.edufocus.edufocus.user.model.entity.User;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Comment {
    @Id
    @GeneratedValue
    private long id;
    @Column
    private String content;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime modifiedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "board_id")
    Board board;

    public ResponseCommentDto makeCommentDto() {
        return ResponseCommentDto.builder()
                .id(id)
                .name(user.getEmail())
                .content(content)
                .createAt(createdAt)
                .modifiedAt(modifiedAt)
                .build();
    }

}
