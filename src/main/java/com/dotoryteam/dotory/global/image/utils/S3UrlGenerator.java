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

    public String createPresignedUrl(Duration duration , PutObjectRequest putObjectRequest) {
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(duration)
                .putObjectRequest(putObjectRequest)
                .build();

        return s3Presigner.presignPutObject(presignRequest).url().toString();
    }

    public String createCloudFrontUrl(String storedFileName) {
        return cloudFrontDomain + "/" + storedFileName;
    }
}
