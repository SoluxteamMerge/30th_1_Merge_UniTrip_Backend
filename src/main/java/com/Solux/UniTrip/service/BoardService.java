package com.Solux.UniTrip.service;

import com.Solux.UniTrip.common.apiPayload.exception.BaseException;
import com.Solux.UniTrip.common.apiPayload.status.FailureStatus;
import com.Solux.UniTrip.common.jwt.JwtTokenProvider;
import com.Solux.UniTrip.dto.request.BoardRequest;
import com.Solux.UniTrip.dto.response.*;
import com.Solux.UniTrip.entity.*;
import com.Solux.UniTrip.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
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
    private final ImageRepository imageRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public BoardResponse createBoard(BoardRequest request, User user,List<MultipartFile> multipartFiles) {
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
                    // category가 없으면 새로 생성 후 저장
                    PostCategory newCategory = new PostCategory();
                    newCategory.setBoardType(boardType);
                    newCategory.setCategoryName(categoryName);
                    return categoryRepository.save(newCategory);
                });

        // multipartfile 리스트를 s3에 업로드 하여 url 받기
        List<Image> images = new ArrayList<>();
        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            List<String> imageUrls = s3Uploader.uploadReviewImages(multipartFiles, "board");

            //URL 리스트로 Image 엔티티 리스트 생성 (Board는 아직 없으니 null로 세팅)
            for (String url : imageUrls) {
                images.add(new Image(null, url));
            }
        }

        // Board 생성 및 저장
        Board board = new Board(request, user, category, images);
        Board savedBoard = boardRepository.save(board);

        //image 엔티티에 저장 후 한꺼번에 저장
        for (Image image : images) {
            image.setBoard(savedBoard);
        }
        if (!images.isEmpty()) {
            imageRepository.saveAll(images);
        }

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
                .likes(likes.intValue())
                .scrapCount(0)
                .isLiked(isLiked)
                .isScraped(false)
                .imageUrl("")
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

    //인기 키워드 검색 및 리뷰 검색
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
    public BoardResponse updateBoard(Long postId, BoardRequest request, User user, List<MultipartFile> multipartFiles) {
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

        // 원래 이미지 삭제
        List<Image> existingImages = board.getImages();
        for (Image image : existingImages) {
            String imageUrl = image.getImageUrl();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                s3Uploader.deleteFile(imageUrl); // S3에서 삭제
            }
        }
        board.getImages().clear();

        // 새 이미지 업로드
        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            List<String> imageUrls = s3Uploader.uploadReviewImages(multipartFiles, "board");

            List<Image> newImages = new ArrayList<>();
            for (String url : imageUrls) {
                newImages.add(new Image(board, url));
            }

            board.getImages().addAll(newImages);
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

        // s3에서 삭제
        List<Image> images = board.getImages();
        for (Image image : images) {
            String imageUrl = image.getImageUrl();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                s3Uploader.deleteFile(imageUrl);
            }
        }

        // 3. 게시글 삭제
        boardRepository.delete(board);

        return new BoardResponse(200, postId, "리뷰가 성공적으로 삭제되었습니다.");
    }

    // 장소별 필터링 결과 조회
    @Transactional(readOnly = true)
    public List<ReviewResultResponse> getPlacesByRegion(Place.Region region, String token){
        List<Board> boards = boardRepository.findByPlace_Region(region);
        return boards.stream()
                .map(ReviewResultResponse::from)
                .collect(Collectors.toList());
    }



}