package com.Solux.UniTrip.service;

import com.Solux.UniTrip.common.apiPayload.base.ApiResponse;
import com.Solux.UniTrip.common.apiPayload.exception.UploadFailureExpection;
import com.Solux.UniTrip.common.apiPayload.status.FailureStatus;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Uploader {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    //프로필 이미지 업로드
    public String uploadProfileImage(MultipartFile file, String folder) {
        String fileName = folder + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            amazonS3.putObject(bucket, fileName, file.getInputStream(), metadata);

            return amazonS3.getUrl(bucket, fileName).toString();
        } catch (IOException e) {
            throw new UploadFailureExpection(FailureStatus._PROFILEIMAGE_UPLOAD_FAILURE);
        }
    }

    //리뷰 사진 업로드(여러 장)
    public List<String> uploadReviewImages(List<MultipartFile> files, String folder) {
        List<String> fileUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            //업로드 로직은 프로필 사진 업로드 재사용
            if (!file.isEmpty()) {
                String fileUrl = uploadProfileImage(file, folder);
                fileUrls.add(fileUrl);
            }
        }
        return fileUrls;
    }


    //이미지 삭제
    public void deleteFile(String fileUrl) {
        try {
            URI uri = new URI(fileUrl);
            String bucketAndKey = uri.getPath();  // /key/path/filename.jpg 형태
            String key = bucketAndKey.startsWith("/") ? bucketAndKey.substring(1) : bucketAndKey;

            amazonS3.deleteObject(bucket, key);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid file URL", e);
        }
    }
}
