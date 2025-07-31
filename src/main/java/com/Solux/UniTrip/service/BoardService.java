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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final PostCategoryRepository categoryRepository;
    private final GroupRecruitBoardRepository groupRecruitBoardRepository;
    private final BoardLikesRepository boardLikesRepository;
    private final BoardScarpRepository boardScarpRepository;
    private final PlaceRepository placeRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final S3Uploader s3Uploader;
    private final RatingRepository ratingRepository;
    private final SearchKeywordRepository searchKeywordRepository;

    @Transactional
    public BoardResponse createBoard(BoardRequest request, User user,List<MultipartFile> multipartFiles) {
        if (user == null)
            throw new BaseException(FailureStatus._UNAUTHORIZED);

        if (request.getBoardType() == null || request.getBoardType().trim().isEmpty())
            throw new BaseException(FailureStatus._BOARDTYPE_NOT_NULL);

        BoardType boardType;
        try {
            boardType = BoardType.valueOf(request.getBoardType());
        } catch (IllegalArgumentException e) {
            throw new BaseException(FailureStatus._INVAILD_BOARDTYPE);
        }

        String categoryName = request.getCategoryName();
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new BaseException(FailureStatus._CATEGORY_NOT_NULL);
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

        // 장소 저장
        Place place = Place.builder()
                .placeName(request.getPlaceName())
                .address(request.getAddress())
                .kakaoId(request.getKakaoId())
                .categoryGroupName(request.getCategoryGroupName())
                .region(Place.Region.from(request.getRegion()))
                .lat(request.getLat())
                .lng(request.getLng())
                .build();

        Place savedPlace = placeRepository.save(place);

        // multipartfile 리스트를 s3에 업로드 하여 url 받기
        List<Image> images = new ArrayList<>();
        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            List<String> imageUrls = s3Uploader.uploadReviewImages(multipartFiles, "board");

            //URL 리스트로 Image 엔티티 리스트 생성 (Board는 아직 없으니 null로 세팅)
            for (String url : imageUrls) {
                Image image = new Image(null, url, user);
                image.setUser(user);
                images.add(image);
            }
        }

        // Board 생성 및 저장
        Board board = new Board(request, user, category, images, savedPlace);
        System.out.println("✔ board.getPlace().getKakaoId(): " + board.getPlace().getKakaoId());

        Board savedBoard = boardRepository.save(board);

        //image 엔티티에 저장 후 한꺼번에 저장
        for (Image image : images) {
            image.setBoard(savedBoard);
        }
        if (!images.isEmpty()) {
            imageRepository.saveAll(images);
        }

        return new BoardResponse(200, savedBoard.getPostId(), "리뷰가 성공적으로 작성되었습니다.");
    }

    public List<ReviewResultResponse> getAllCard(User user) {
        List<Board> boards = boardRepository.findAll();
        return boards.stream()
                .map(ReviewResultResponse::from)
                .collect(Collectors.toList());
    }


    public BoardListResponse getCardsByBoardType(BoardType boardType, User user) {
        List<Board> boards = boardRepository.findByBoardType(boardType);
        return convertToBoardListResponse(boards, user);
    }

    // 단건 조회
    @Transactional
    public BoardItemResponse getBoardById(Long postId, User user, boolean increaseView) {
        Board board = boardRepository.findById(postId)
                .orElseThrow(() -> new BaseException(FailureStatus._POST_NOT_FOUND));

        if (increaseView)
            board.setViews(board.getViews() + 1);

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
        Long scraps = boardScarpRepository.countByBoard(board);

        // 기본값 false
        boolean isLiked = false;
        boolean isScraped = false;

        // 로그인한 사용자라면 내가 좋아요 눌렀는지 확인
        if (user != null) {
            isLiked = boardLikesRepository.findByBoardAndUserAndStatus(board, user, true)
                    .isPresent();
            isScraped = boardScarpRepository.findByBoardAndUser(board, user)
                    .isPresent();
        }

        List<Image> images = board.getImages();
        String imageUrl = (images != null && !images.isEmpty()) ? images.get(0).getImageUrl() : null;

        // 평균 평점 계산
        List<Rating> ratings = board.getRatings();
        double averageRating = 0.0;
        if (ratings != null && !ratings.isEmpty()) {
            averageRating = ratings.stream()
                    .mapToDouble(Rating::getRating)
                    .average()
                    .orElse(0.0);

            // 소수점 첫째 자리로 반올림
            averageRating = Math.round(averageRating * 10) / 10.0;
        }

        BoardItemResponse.BoardItemResponseBuilder builder = BoardItemResponse.builder()
                .postId(board.getPostId())
                .boardType(board.getBoardType().toString())
                .categoryName(board.getCategory().getCategoryName())
                .title(board.getTitle())
                .content(board.getContent())
                .userId(board.getUser().getUserId())
                .nickname(board.getUser().getNickname())
                .profileImageUrl(board.getUser().getProfileImageUrl())
                .createdAt(board.getCreatedAt().toString())
                .views(board.getViews())
                .rating(averageRating)
                .likes(likes.intValue())
                .scrapCount(scraps.intValue())
                .isLiked(isLiked)
                .isScraped(isScraped)
                .imageUrl(imageUrl)
                .placeName(board.getPlace().getPlaceName())
                .address(board.getPlace().getAddress())
                .kakaoId(board.getPlace().getKakaoId())
                .categoryGroupName(board.getPlace().getCategoryGroupName())
                .region(String.valueOf(board.getPlace().getRegion()));

        return builder.build();
    }

    //인기 키워드 검색 및 리뷰 검색
    @Transactional(readOnly = true)
    public List<ReviewResultResponse> searchResults(String keyword, String token, String sort) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new IllegalArgumentException("Invalid Token");
        }
        String email = jwtTokenProvider.getEmailFromToken(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BaseException(FailureStatus._USER_NOT_FOUND));

        String processedKeyword = "%" + keyword.trim().toLowerCase() + "%";

        List<Board> boards;

        // sort 값에 따라 다르게 쿼리 호출
        if ("popular".equalsIgnoreCase(sort)) {
            boards = boardRepository.searchByKeywordOrderByLikesDesc(keyword);
        } else if ("latest".equalsIgnoreCase(sort)) {
            boards = boardRepository.searchByKeywordOrderByCreatedAtDesc(keyword);
        } else {
            boards = boardRepository.searchByKeyword(keyword); // 기본 검색 (정렬 없음)
        }

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
            throw new BaseException(FailureStatus._UNAUTHORIZED);

        if (!board.getUser().getUserId().equals(user.getUserId()))
            throw new BaseException(FailureStatus.FORBIDDEN);

        // 3. 내용 수정
        // categoryName → 실제 Category 엔티티 찾아서 설정
        PostCategory category = categoryRepository.findByBoardTypeAndCategoryName(board.getBoardType(), request.getCategoryName())
                .orElseThrow(() -> new BaseException(FailureStatus._CATEGORY_NOT_FOUND));

        // 공통 필드 업데이트
        board.updateCommonFields(
                request.getTitle(),
                request.getContent(),
                category
        );

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
                Image image = new Image(board, url, user);
                image.setUser(user);
                newImages.add(image);
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
        Board board = boardRepository.findById(postId).orElseThrow(() -> new BaseException(FailureStatus._POST_NOT_FOUND));

        // 2. 작성자인지 확인
        if (user == null)
            throw new BaseException(FailureStatus._UNAUTHORIZED);

        if (!board.getUser().getUserId().equals(user.getUserId()))
            throw new BaseException(FailureStatus.FORBIDDEN);

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

    //별점 등록/삭제
    @Transactional
    public RatingResponse toggleRating(Long postId, Long userId, Double newRating) {
        // 유효성 체크 (.5 단위 필터링)
        if (newRating % 0.5 != 0) {
            throw new BaseException(FailureStatus.RATING_INVALID);
        }

        Board board = boardRepository.findById(postId)
                .orElseThrow(() -> new BaseException(FailureStatus._POST_NOT_FOUND));

        /*// 본인 검증
        if (!board.getUser().getUserId().equals(userId)) {
            throw new BaseException(FailureStatus.FORBIDDEN);
        }*/

        // userId 기준으로 별점 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(FailureStatus._USER_NOT_FOUND));

        Optional<Rating> existing = ratingRepository.findByBoardAndUser(board, user);

        if (existing.isPresent()) {
            Rating rating = existing.get();
            if (rating.getRating().equals(newRating)) {
                ratingRepository.delete(rating);
                return new RatingResponse(postId, null, false);
            } else {
                rating.setRating(newRating);
                return new RatingResponse(postId, newRating, true);
            }
        } else {
            Rating rating = Rating.builder()
                    .board(board)
                    .user(user)
                    .rating(newRating)
                    .build();
            ratingRepository.save(rating);
            return new RatingResponse(postId, newRating, true);
        }
    }
}