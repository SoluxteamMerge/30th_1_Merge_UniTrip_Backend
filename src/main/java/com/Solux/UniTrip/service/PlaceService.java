package com.Solux.UniTrip.service;
//실제 카카오맵 서비스 api를 호출해서 장소 검색 결과를 받아오는 서비스
//받아온 Json 데이터를 placesearchresponse 객체 리스트로 변환해서 컨트롤러에 넘겨줌

import com.Solux.UniTrip.common.apiPayload.exception.BaseException;
import com.Solux.UniTrip.common.apiPayload.status.FailureStatus;
import com.Solux.UniTrip.common.jwt.JwtTokenProvider;
import com.Solux.UniTrip.dto.response.BoardListResponse;
import com.Solux.UniTrip.dto.response.PlaceResponse;
import com.Solux.UniTrip.dto.response.ReviewResultResponse;
import com.Solux.UniTrip.entity.Board;
import com.Solux.UniTrip.entity.User;
import com.Solux.UniTrip.repository.BoardRepository;
import com.Solux.UniTrip.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final JwtTokenProvider jwtTokenProvider;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Value("${kakao.rest-api-key}")
    private String kakaoApikey;

    //장소 검색 메서드
    public List<PlaceResponse> registerPlaces(String keyword, String accessToken) {

        //String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
        String url = "https://dapi.kakao.com/v2/local/search/keyword.json?query=" + keyword;

        //요청 헤더에 카카오 api key 넣기
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApikey);

        //바디 없음
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        //RestTemplate 객체 생성 (HTTP 요청 보내기 도구)
        RestTemplate restTemplate = new RestTemplate();

        //카카오 API 호출
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        System.out.println("카카오 응답: " + response.getBody());

        //결과 파싱
        JSONObject json = new JSONObject(response.getBody());
        JSONArray documents = json.getJSONArray("documents");

        //결과를 담을 리스트 생성
        List<PlaceResponse> results = new ArrayList<>();

        //JSON 배열 순회하면서 PlaceSearchResponse 객체 생성 후 리스트에 추가
        for (int i = 0; i < documents.length(); i++) {
            JSONObject obj = documents.getJSONObject(i);

            PlaceResponse place = PlaceResponse.builder()
                    .placeName(obj.getString("place_name"))
                    .address(obj.getString("address_name"))
                    .kakaoId(Integer.parseInt(obj.getString("id")))
                    .build();

            results.add(place);
        }

        //완성된 장소 리스트 반환
        return results;
    }



}
