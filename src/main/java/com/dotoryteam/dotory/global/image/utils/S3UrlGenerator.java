package com.dotoryteam.dotory.global.image.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class S3UrlGenerator {
    @Value("${spring.cloud.aws.cloudfront.domain}")
    private String cloudFrontDomain;

    private final S3Presigner s3Presigner;

    //presigned url 생성
    public String createPresignedUrl(PutObjectRequest putObjectRequest) {
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5))
                .putObjectRequest(putObjectRequest)
                .build();

        return s3Presigner.presignPutObject(presignRequest).url().toString();
    }

    //cloud front url 생성
    public String createCloudFrontUrl(String storedFileName) {
        return cloudFrontDomain + "/" + storedFileName;
    }
}
