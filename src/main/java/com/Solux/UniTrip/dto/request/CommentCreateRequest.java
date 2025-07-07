package com.Solux.UniTrip.dto.request;
//댓글 생성 Reqeust DTO
//백다현

import lombok.Getter;

@Getter
public class CommentCreateRequest {
    private Long postId;
    private String content;
}