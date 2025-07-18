package com.Solux.UniTrip.service;

import com.Solux.UniTrip.common.apiPayload.exception.BaseException;
import com.Solux.UniTrip.common.apiPayload.status.FailureStatus;
import com.Solux.UniTrip.common.jwt.JwtTokenProvider;
import com.Solux.UniTrip.dto.request.BoardRequest;
import com.Solux.UniTrip.dto.response.BoardItemResponse;
import com.Solux.UniTrip.dto.response.BoardListResponse;
import com.Solux.UniTrip.dto.response.BoardResponse;
import com.Solux.UniTrip.dto.response.ReviewResultResponse;
import com.Solux.UniTrip.entity.Board;
import com.Solux.UniTrip.entity.BoardType;
import com.Solux.UniTrip.entity.GroupRecruitBoard;
import com.Solux.UniTrip.entity.PostCategory;
import com.Solux.UniTrip.entity.User;
import com.Solux.UniTrip.repository.BoardLikesRepository;
import com.Solux.UniTrip.repository.BoardRepository;
import com.Solux.UniTrip.repository.GroupRecruitBoardRepository;
import com.Solux.UniTrip.repository.PostCategoryRepository;
import jakarta.transaction.Transactional;
import com.Solux.UniTrip.repository.UserRepository;
import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final PostCategoryRepository categoryRepository;
    private final GroupRecruitBoardRepository groupRecruitBoardRepository;
    private final BoardLikesRepository boardLikesRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PostCategoryRepository postCategoryRepository;

    public BoardResponse createBoard(BoardRequest request, User user) {
        if (user == null) {
            throw new RuntimeException("User cannot be null");
        }

        if (request.getBoardType() == null || request.getBoardType().trim().isEmpty()) {
            throw new RuntimeException("BoardType must not be empty");
        }

        BoardType boardType;
        try {
            boardType = BoardType.valueOf(request.getBoardType());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid BoardType: " + request.getBoardType());
        }

        String categoryName = request.getCategoryName();
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new RuntimeException("Category name must not be empty");
        }

        // boardType이 "모임구인"이면 추가 필드 검증
        if (boardType == BoardType.모임구인) {
            if (request.getOvernightFlag() == null) {
                throw new RuntimeException("overnightFlag must not be null for 모임구인 type");
            }
            if (request.getRecruitmentCnt() == null) {
                throw new RuntimeException("recruitmentCnt must not be null for 모임구인 type");
            }
        }

        // DB 조회
        PostCategory category = categoryRepository.findByBoardTypeAndCategoryName(boardType, categoryName)
                .orElseGet(() -> {
                    PostCategory newCategory = new PostCategory();
                    newCategory.setBoardType(boardType);
                    newCategory.setCategoryName(categoryName);
                    return categoryRepository.save(newCategory);
                });

        // Board 생성 및 저장
        Board board = new Board(request, user, category);
        Board savedBoard = boardRepository.save(board);

        // 모임구인 추가 정보 저장
        if (boardType == BoardType.모임구인) {
            GroupRecruitBoard groupRecruitBoard = new GroupRecruitBoard(
                    savedBoard,
                    request.getOvernightFlag(),
                    request.getRecruitmentCnt()
            );
            groupRecruitBoardRepository.save(groupRecruitBoard);
        }

        return new BoardResponse(200, savedBoard.getPostId(), "리뷰가 성공적으로 작성되었습니다.");
    }

    public BoardListResponse getAllCard(User user) {
        List<Board> boards = boardRepository.findAll();
        return convertToBoardListResponse(boards, user);
    }

    public BoardListResponse getCardsByBoardType(BoardType boardType, User user) {
        List<Board> boards = boardRepository.findByBoardType(boardType);
        return convertToBoardListResponse(boards, user);
    }

    // 단건 조회
    public BoardItemResponse getBoardById(Long postId, User user) {
        Board board = boardRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        return convertToBoardItemResponse(board, user);
    }

    private BoardListResponse convertToBoardListResponse(List<Board> boards, User user) {
        List<BoardItemResponse> items = boards.stream()
                .map(board -> convertToBoardItemResponse(board, user))
                .toList();

        return BoardListResponse.builder()
                .total(items.size())
                .reviews(items)
                .build();
    }

    private BoardItemResponse convertToBoardItemResponse(Board board, User user) {
        Long likes = boardLikesRepository.countByBoardAndStatus(board, true);

        // 기본값 false
        boolean isLiked = false;

        // 로그인한 사용자라면 내가 좋아요 눌렀는지 확인
        if (user != null)
            isLiked = boardLikesRepository.findByBoardAndUserAndStatus(board, user, true)
                    .isPresent();


        return BoardItemResponse.builder()
                .postId(board.getPostId())
                .boardType(board.getBoardType().toString())
                .categoryName(board.getCategory().getCategoryName())
                .title(board.getTitle())
                .content(board.getContent())
                .userId(board.getUser().getUserId())
                .nickname(board.getUser().getNickname())
                .createdAt(board.getCreatedAt().toString())
                .commentCount(0)
                .likes(likes.intValue())
                .scrapCount(0)
                .isLiked(isLiked)
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

    @Transactional(readOnly = true)
    public List<ReviewResultResponse> searchResults(String keyword, String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new IllegalArgumentException("Invalid Token");
        }
        String email = jwtTokenProvider.getEmailFromToken(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BaseException(FailureStatus._USER_NOT_FOUND));

        String processedKeyword = "%" + keyword.trim().toLowerCase() + "%";

        List<Board> boards = boardRepository.searchByKeyword(keyword);

        return boards.stream()
                .map(ReviewResultResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public BoardResponse updateBoard(Long postId, BoardRequest request, User user) {
        // 1. 기존 게시글 조회
        Board board = boardRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 2. 작성자인지 확인
        if (user == null)
            throw new RuntimeException("User cannot be null");

        if (!board.getUser().getUserId().equals(user.getUserId()))
            throw new SecurityException("수정 권한이 없습니다.");

        // 3. 내용 수정
        // categoryName → 실제 Category 엔티티 찾아서 설정
        PostCategory category = categoryRepository.findByBoardTypeAndCategoryName(board.getBoardType(), request.getCategoryName())
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

        // 공통 필드 업데이트
        board.updateCommonFields(
                request.getTitle(),
                request.getContent(),
                category
        );

        // 모임구인 게시판이면 GroupRecruitBoard 도 수정
        if (BoardType.모임구인.equals(board.getBoardType())) {
            GroupRecruitBoard groupRecruit = groupRecruitBoardRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("모임구인 추가 정보를 찾을 수 없습니다."));
            groupRecruit.updateRecruitFields(
                    request.getOvernightFlag(),
                    request.getRecruitmentCnt()
            );
        }

        // 4. 수정된 게시글을 저장 (JPA에서는 트랜잭션 끝나면 자동 반영)
        // boardRepository.save(board); // 필요없음.

        // 5. 응답 DTO로 변환
        return new BoardResponse(200, postId, "리뷰가 성공적으로 수정되었습니다.");
    }

    public BoardResponse deleteBoard(Long postId, User user) {
        // 1. 기존 게시글 조회
        Board board = boardRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 2. 작성자인지 확인
        if (user == null)
            throw new RuntimeException("User cannot be null");

        if (!board.getUser().getUserId().equals(user.getUserId()))
            throw new SecurityException("수정 권한이 없습니다.");

        // 3. 게시글 삭제
        boardRepository.delete(board);

        return new BoardResponse(200, postId, "리뷰가 성공적으로 삭제되었습니다.");
    }
}