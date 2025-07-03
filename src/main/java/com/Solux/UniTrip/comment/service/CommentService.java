package com.Solux.UniTrip.comment.service;

import com.Solux.UniTrip.comment.dto.CommentCreateRequestDto;
import com.Solux.UniTrip.comment.dto.CommentResponseDto;
import com.Solux.UniTrip.comment.dto.CommentUpdateRequestDto;
import com.Solux.UniTrip.comment.dto.CommentUpdateResponseDto;
import com.Solux.UniTrip.Entity.Comment;
import com.Solux.UniTrip.comment.repository.CommentRepository;
import com.Solux.UniTrip.common.dto.PageResponseDto;
import com.Solux.UniTrip.common.exception.NotFoundException;
import com.Solux.UniTrip.common.exception.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentResponseDto createComment(CommentCreateRequestDto dto) {
        if (dto == null || dto.getPostId() == null || dto.getContent() == null ||
                dto.getContent().trim().isEmpty()) {
            throw new BadRequestException("요청 값이 올바르지 않습니다.");
        }


        // 실제로는 Post 존재 여부를 확인해야 함 (PostRepository.findById)
        boolean postExists = true; // TODO: Post 존재 체크
        if (!postExists) {
            throw new NotFoundException("해당 게시글을 찾을 수 없습니다.");
        }

        Comment comment = new Comment(
                dto.getPostId(),
                null, // 로그인 구현 후 userId 설정
                dto.getContent()
        );

        Comment saved = commentRepository.save(comment);

        return CommentResponseDto.builder()
                .commentId(saved.getCommentId())
                .postId(saved.getPostId())
                .content(saved.getContent())
                .author("작성자") // 로그인 개발 후 유저 이름
                .createdAt(saved.getCreatedAt())
                .build();
    }

    // 댓글 수정
    @Transactional
    public CommentUpdateResponseDto updateComment(Long commentId, CommentUpdateRequestDto dto) {
        if (dto == null || dto.getContent() == null || dto.getContent().trim().isEmpty()) {
            throw new BadRequestException("요청 값이 올바르지 않습니다.");
        }


        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("해당 댓글을 찾을 수 없습니다."));

        // (추후) 작성자 권한 체크 필요

        comment.updateContent(dto.getContent());

        return CommentUpdateResponseDto.builder()
                .commentId(comment.getCommentId())
                .content(comment.getContent())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("해당 댓글을 찾을 수 없습니다."));

        // (추후) 현재 유저와 작성자 비교해서 권한 체크 필요
        commentRepository.delete(comment);
    }

    //댓글 목록 조회
    //댓글 목록 조회
    public PageResponseDto<CommentResponseDto> getComments(Long postId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Comment> commentPage = commentRepository.findByPostId(postId, pageable);

        List<CommentResponseDto> content = commentPage.getContent().stream()
                .map(CommentResponseDto::from)
                .toList();

        return PageResponseDto.<CommentResponseDto>builder()
                .content(content)
                .page(page)
                .size(size)
                .totalElements(commentPage.getTotalElements())
                .totalPages(commentPage.getTotalPages())
                .build();
    }
}