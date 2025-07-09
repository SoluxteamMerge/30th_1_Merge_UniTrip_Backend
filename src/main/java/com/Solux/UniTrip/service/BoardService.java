package com.Solux.UniTrip.service;

import com.Solux.UniTrip.dto.request.BoardRequest;
import com.Solux.UniTrip.dto.response.BoardResponse;
import com.Solux.UniTrip.entity.Board;
import com.Solux.UniTrip.entity.BoardType;
import com.Solux.UniTrip.entity.PostCategory;
import com.Solux.UniTrip.entity.User;
import com.Solux.UniTrip.repository.BoardRepository;
import com.Solux.UniTrip.repository.PostCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final PostCategoryRepository categoryRepository;

    public BoardResponse createBoard(BoardRequest request, User user) {
        if (user == null) {
            throw new RuntimeException("User cannot be null");
        }

        // BoardType enum 값 변환
        BoardType boardType = BoardType.valueOf(request.getBoardType());

        // boardType과 categoryName을 같이 조건으로 카테고리 조회
        PostCategory category = categoryRepository.findByBoardTypeAndCategoryName(boardType, request.getCategoryName())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Board 엔티티의 생성자 사용
        Board board = new Board(request, user, category);

        Board saved = boardRepository.save(board);
        return new BoardResponse(200, saved.getPostId(), "리뷰가 성공적으로 작성되었습니다.");
    }
}