package com.Solux.UniTrip.service;

import com.Solux.UniTrip.dto.request.BoardRequest;
import com.Solux.UniTrip.dto.response.BoardItemResponse;
import com.Solux.UniTrip.dto.response.BoardListResponse;
import com.Solux.UniTrip.dto.response.BoardResponse;
import com.Solux.UniTrip.entity.Board;
import com.Solux.UniTrip.entity.BoardType;
import com.Solux.UniTrip.entity.GroupRecruitBoard;
import com.Solux.UniTrip.entity.PostCategory;
import com.Solux.UniTrip.entity.User;
import com.Solux.UniTrip.repository.BoardRepository;
import com.Solux.UniTrip.repository.GroupRecruitBoardRepository;
import com.Solux.UniTrip.repository.PostCategoryRepository;
import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final PostCategoryRepository categoryRepository;
    private final GroupRecruitBoardRepository groupRecruitBoardRepository;

    public BoardResponse createBoard(BoardRequest request, User user) {
        if (user == null) {
            throw new RuntimeException("User cannot be null");
        }

        BoardType boardType = BoardType.valueOf(request.getBoardType()); // enum 사용

        // categoryName을 enum 없이 문자열로 찾기
        String categoryName = request.getCategoryName();

        // categoryName이 비어있으면 저장 안 함
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new RuntimeException("Category name must not be empty");
        }

        // boardType과 categoryName으로 카테고리 찾기
        PostCategory category = categoryRepository.findByBoardTypeAndCategoryName(boardType, categoryName)
                .orElseGet(() -> {
                    // category가 없으면 새로 생성 후 저장
                    PostCategory newCategory = new PostCategory();
                    newCategory.setBoardType(boardType);
                    newCategory.setCategoryName(categoryName);
                    return categoryRepository.save(newCategory);
                });

        // Board 생성
        Board board = new Board(request, user, category);
        Board savedBoard = boardRepository.save(board);

        // boardType이 "모임구인"이면 GroupRecruitBoard 저장
        if (boardType == BoardType.모임구인) {
            if (request.getOvernightFlag() == null || request.getRecruitmentCnt() == null) {
                throw new RuntimeException("overnightFlag and recruitmentCnt must not be null for 모임구인 type");
            }
            GroupRecruitBoard groupRecruitBoard = new GroupRecruitBoard(
                    savedBoard,
                    request.getOvernightFlag(),
                    request.getRecruitmentCnt()
            );
            groupRecruitBoardRepository.save(groupRecruitBoard);
        }

        return new BoardResponse(200, savedBoard.getPostId(), "리뷰가 성공적으로 작성되었습니다.");
    }

    public BoardListResponse getAllCard() {
        List<Board> boards = boardRepository.findAll();
        return convertToBoardListResponse(boards);
    }

    public BoardListResponse getCardsByBoardType(BoardType boardType) {
        List<Board> boards = boardRepository.findByBoardType(boardType);
        return convertToBoardListResponse(boards);
    }

    // 단건 조회
    public BoardItemResponse getBoardById(Long postId) {
        Board board = boardRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        return convertToBoardItemResponse(board);
    }

    private BoardListResponse convertToBoardListResponse(List<Board> boards) {
        List<BoardItemResponse> items = boards.stream()
                .map(this::convertToBoardItemResponse)
                .toList();

        return BoardListResponse.builder()
                .total(items.size())
                .reviews(items)
                .build();
    }

    private BoardItemResponse convertToBoardItemResponse(Board board) {
        BoardItemResponse.BoardItemResponseBuilder builder = BoardItemResponse.builder()
                .postId(board.getPostId())
                .boardType(board.getBoardType().toString())
                .categoryName(board.getCategory().getCategoryName())
                .title(board.getTitle())
                .content(board.getContent())
                .userId(board.getUser().getUserId())
                .nickname(board.getUser().getNickname())
                .createdAt(board.getCreatedAt().toString())
                .commentCount(0)
                .likes(board.getLikes())
                .scraps(0)
                .isLiked(false)
                .isScraped(false)
                .thumbnailUrl("")
                .placeName(board.getPlaceName())
                .roadAddress(board.getRoadAddress())
                .kakaoPlaceId(board.getKakaoPlaceId())
                .latitude(board.getLatitude())
                .longitude(board.getLongitude());

        if (BoardType.모임구인.equals(board.getBoardType())) {
            groupRecruitBoardRepository.findById(board.getPostId())
                    .ifPresent(groupRecruit -> {
                        builder.overnightFlag(groupRecruit.getOvernightFlag());
                        builder.recruitmentCnt(groupRecruit.getRecruitmentCnt());
                    });
        }

        return builder.build();
    }
}