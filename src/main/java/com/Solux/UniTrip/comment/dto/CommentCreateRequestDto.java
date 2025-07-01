package com.Solux.UniTrip.comment.dto;
//댓글 생성 Reqeust DTO
//백다현

import lombok.Getter;

@Getter
public class CommentCreateRequestDto {
    private Long postId;
    private String content;
}