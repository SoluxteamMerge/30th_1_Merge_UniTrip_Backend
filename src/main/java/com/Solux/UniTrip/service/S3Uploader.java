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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Uploader {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    //이미지 업로드
    public String uploadFile(MultipartFile file, String folder) {
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
