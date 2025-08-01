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

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final PostCategoryRepository categoryRepository;
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

        return new BoardResponse(200, savedBoard.getPostId(), "리뷰가 성공적으로 작성되었습니다.", null);
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
                .scheduleDate(board.getScheduleDate())
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
        // 1. 게시글 조회
        Board board = boardRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 2. 작성자 확인
        if (user == null) throw new BaseException(FailureStatus._UNAUTHORIZED);
        if (!board.getUser().getUserId().equals(user.getUserId()))
            throw new BaseException(FailureStatus.FORBIDDEN);

        // 3. boardType과 categoryName 처리
        final BoardType resolvedBoardType =
                (request.getBoardType() != null && !request.getBoardType().isBlank())
                        ? BoardType.valueOf(request.getBoardType().trim())
                        : board.getCategory().getBoardType();

        final String resolvedCategoryName =
                (request.getCategoryName() != null && !request.getCategoryName().isBlank())
                        ? request.getCategoryName()
                        : board.getCategory().getCategoryName();

        PostCategory category = categoryRepository
                .findByBoardTypeAndCategoryName(resolvedBoardType, resolvedCategoryName)
                .orElseGet(() -> {
                    PostCategory newCategory = new PostCategory();
                    newCategory.setBoardType(resolvedBoardType);
                    newCategory.setCategoryName(resolvedCategoryName);
                    return categoryRepository.save(newCategory);
                });

        // 4. 필드 업데이트
        board.updateCommonFields(
                request.getTitle() != null ? request.getTitle() : board.getTitle(),
                request.getContent() != null ? request.getContent() : board.getContent(),
                category
        );

        // 5. 일정(scheduleDate) 업데이트
        if (request.getScheduleDate() != null && !request.getScheduleDate().isBlank()) {
            board.setScheduleDate(request.getScheduleDate());
        }

        // 6. 이미지 교체
        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            // 기존 이미지 삭제
            List<Image> existingImages = board.getImages();
            for (Image image : existingImages) {
                String imageUrl = image.getImageUrl();
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    s3Uploader.deleteFile(imageUrl);
                }
            }
            board.getImages().clear();

            // 새 이미지 업로드
            List<String> imageUrls = s3Uploader.uploadReviewImages(multipartFiles, "board");
            List<Image> newImages = new ArrayList<>();
            for (String url : imageUrls) {
                Image image = new Image(board, url, user);
                image.setUser(user);
                newImages.add(image);
            }
            board.getImages().addAll(newImages);
        }

        // 7. 장소 수정
        if (request.getPlaceName() != null && !request.getPlaceName().isBlank()) {
            Place currentPlace = board.getPlace();
            if (currentPlace == null || !currentPlace.getPlaceName().equals(request.getPlaceName())) {
                Place newPlace = placeRepository.findByPlaceName(request.getPlaceName())
                        .orElseGet(() -> {
                            Place place = new Place();
                            place.setPlaceName(request.getPlaceName());
                            place.setAddress(request.getAddress());
                            place.setKakaoId(request.getKakaoId());
                            place.setCategoryGroupName(request.getCategoryGroupName());

                            if (request.getRegion() != null && !request.getRegion().isBlank()) {
                                place.setRegion(Place.Region.valueOf(request.getRegion().toUpperCase().trim()));
                            }
                            if (request.getLat() != null) place.setLat(request.getLat());
                            if (request.getLng() != null) place.setLng(request.getLng());

                            return placeRepository.save(place);
                        });
                board.setPlace(newPlace);
            }
        }

        // 8. 수정 시간은 @LastModifiedDate에 의해 자동 갱신됨
        boardRepository.save(board);
        // 9. 응답 반환
        return new BoardResponse(
                200,
                postId,
                "리뷰가 성공적으로 수정되었습니다.",
                board.getUpdatedAt() != null
                        ? board.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                        : null
        );
    }



    public void deleteBoard(Long postId, User user) {
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

    }

    // 장소별 필터링 결과 조회
    @Transactional(readOnly = true)
    public List<ReviewResultResponse> getPlacesByRegion(Place.Region region, String token){
        List<Board> boards = boardRepository.findByPlace_Region(region);
        return boards.stream()
                .map(ReviewResultResponse::from)
                .collect(Collectors.toList());
    }

    // 전체보기 조회
    @Transactional(readOnly = true)
    public List<ReviewResultResponse> getAllPlaces(String token){
        List<Board> boards = boardRepository.findAll(); // 전체 조회
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