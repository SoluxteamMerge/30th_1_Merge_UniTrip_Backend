package com.Solux.UniTrip.service;
//실제 카카오맵 서비스 api를 호출해서 장소 검색 결과를 받아오는 서비스
//받아온 Json 데이터를 placesearchresponse 객체 리스트로 변환해서 컨트롤러에 넘겨줌

import com.Solux.UniTrip.common.jwt.JwtTokenProvider;
import com.Solux.UniTrip.dto.response.PlaceResponse;
import com.Solux.UniTrip.entity.Place;
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

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final JwtTokenProvider jwtTokenProvider;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    private Place.Region extractRegionFromAddress(String address) {
        if (address == null || address.isEmpty()) return null;

        String[] tokens = address.split(" ");
        if (tokens.length == 0) return null;

        String regionStr = tokens[0];
        switch (regionStr) {
            case "서울":
            case "서울특별시":
                return Place.Region.SEOUL;
            case "인천":
            case "인천광역시":
                return Place.Region.INCHEON;
            case "대전":
            case "대전광역시":
                return Place.Region.DAEJEON;
            case "대구":
            case "대구광역시":
                return Place.Region.DAEGU;
            case "광주":
            case "광주광역시":
                return Place.Region.GWANGJU;
            case "부산":
            case "부산광역시":
                return Place.Region.BUSAN;
            case "울산":
            case "울산광역시":
                return Place.Region.ULSAN;
            case "세종":
            case "세종특별자치시":
                return Place.Region.SEJONG;
            case "경기":
            case "경기도":
                return Place.Region.GYEONGGI;
            case "강원":
            case "강원도":
            case "강원특별자치도":
                return Place.Region.GANGWON;
            case "충북":
            case "충청북도":
                return Place.Region.CHUNGBUK;
            case "충남":
            case "충청남도":
                return Place.Region.CHUNGNAM;
            case "경북":
            case "경상북도":
                return Place.Region.GYEONGBUK;
            case "경남":
            case "경상남도":
                return Place.Region.GYEONGNAM;
            case "전북":
            case "전라북도":
                return Place.Region.JEONBUK;
            case "전남":
            case "전라남도":
                return Place.Region.JEONNAM;
            case "제주":
            case "제주특별자치도":
                return Place.Region.JEJU;
            default:
                return null;
        }

    }


    @Value("${kakao.rest-api-key}")
    private String kakaoApikey;

    //장소 검색 메서드
    public List<PlaceResponse> registerPlaces(String keyword, String token) {

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

            String address = obj.getString("address_name");
            Place.Region region = extractRegionFromAddress(address);
            // region 이 없으면 ETC
            if (region == null) {
                region = Place.Region.ETC;
            }

            PlaceResponse place = PlaceResponse.builder()
                    .placeName(obj.getString("place_name"))
                    .address(address)
                    .kakaoId(Integer.parseInt(obj.getString("id")))
                    .categoryGroupName(obj.getString("category_group_name"))
                    .region(region)
                    .lat(Float.parseFloat(obj.getString("y")))
                    .lng(Float.parseFloat(obj.getString("x")))
                    .build();

            results.add(place);
        }

        return results;
    }
}




