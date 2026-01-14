package com.dotoryteam.dotory.global.image.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Presigner s3Presigner;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${spring.cloud.aws.cloudfront.domain}")
    private String cloudFrontDomain;

    //presigned 주소 받기
    public String getPresignedUrl(String prefix, String originalFilename) {
        //파일 확장자 처리
        String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        if (!ext.matches("jpg|jpeg|png|gif")) {
            throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
        }

        // 2. MIME Type 설정하기
        String contentType = "image/" + (ext.equals("jpg") ? "jpeg" : ext);
        String fileName = prefix + "/" + UUID.randomUUID() + "-" + originalFilename;

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .contentType(contentType)
                .build();

        // 서명된 URL 생성
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                //업로드 시간 5분 내로
                .signatureDuration(Duration.ofMinutes(5))
                .putObjectRequest(objectRequest)
                .build();

        return s3Presigner.presignPutObject(presignRequest).url().toString();
    }

    // 2. DB에 저장하거나 프론트엔드에 보여줄 때는 CloudFront URL 반환
    public String getCloudFrontUrl(String storedFileName) {
        //https://d1234.cloudfront.net/profile/uuid-image.jpg
        return cloudFrontDomain + "/" + storedFileName;
    }
}