package com.dotoryteam.dotory.global.image.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
@RequiredArgsConstructor
public class S3Uploader {
    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public PutObjectRequest createPutObjectRequest(String key , String contentType) {
        return PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .build();
    }
}