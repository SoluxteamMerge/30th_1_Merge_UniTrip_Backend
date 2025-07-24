package com.Solux.UniTrip.service;

import com.Solux.UniTrip.common.apiPayload.exception.BaseException;
import com.Solux.UniTrip.common.apiPayload.status.FailureStatus;
import com.Solux.UniTrip.dto.response.BoardResponse;
import com.Solux.UniTrip.entity.Board;
import com.Solux.UniTrip.entity.Scrap;
import com.Solux.UniTrip.entity.User;
import com.Solux.UniTrip.repository.BoardRepository;
import com.Solux.UniTrip.repository.BoardScarpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardScrapService {
    private final BoardScarpRepository boardScarpRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public BoardResponse toggleScrap(Long postId, User user) {
        if (user == null)
            throw new BaseException(FailureStatus._UNAUTHORIZED);

        Board board = boardRepository.findById(postId)
                .orElseThrow(() -> new BaseException(FailureStatus._POST_NOT_FOUND));

        Scrap existingScrap = boardScarpRepository.findByBoardAndUser(board, user)
                .orElse(null);

        String message;

        if (existingScrap == null) {
            Scrap newScrap = Scrap.builder()
                    .board(board)
                    .user(user)
                    .build();
            boardScarpRepository.save(newScrap);
            message = "북마크를 눌렀습니다.";
        } else {
            boardScarpRepository.delete(existingScrap);
            message = "북마크를 취소했습니다.";
        }

        return new BoardResponse(200, postId, message);
    }
}