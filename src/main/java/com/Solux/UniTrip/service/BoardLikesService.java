package com.Solux.UniTrip.service;

import com.Solux.UniTrip.common.apiPayload.exception.BaseException;
import com.Solux.UniTrip.common.apiPayload.status.FailureStatus;
import com.Solux.UniTrip.dto.response.BoardResponse;
import com.Solux.UniTrip.entity.Board;
import com.Solux.UniTrip.entity.PostLikes;
import com.Solux.UniTrip.entity.User;
import com.Solux.UniTrip.repository.BoardLikesRepository;
import com.Solux.UniTrip.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BoardLikesService {
    private final BoardLikesRepository boardLikesRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public BoardResponse toggleLike(Long postId, User user) {
        if (user == null)
            throw new BaseException(FailureStatus._UNAUTHORIZED);

        Board board = boardRepository.findById(postId)
                .orElseThrow(() -> new BaseException(FailureStatus._POST_NOT_FOUND));

        PostLikes existingLike = boardLikesRepository.findByBoardAndUserAndStatus(board, user, true)
                .orElse(null);

        String message;

        if (existingLike == null) {
            // 좋아요 추가 (status=1)
            PostLikes newLike = PostLikes.builder()
                    .board(board)
                    .user(user)
                    .likedAt(LocalDateTime.now())
                    .status(true)  // status 필드는 항상 true로 저장
                    .build();
            boardLikesRepository.save(newLike);
            message = "좋아요를 눌렀습니다.";
        } else {
            // 좋아요 취소 → DB에서 삭제
            boardLikesRepository.delete(existingLike);
            message = "좋아요를 취소했습니다.";
        }

        return new BoardResponse(200, postId, message);
    }
}