package com.dotoryteam.dotory.global.image.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class S3FilePathResolver {
    public String createPath(String prefix, String originalFileName) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        String uuid = UUID.randomUUID().toString();
        return prefix + "/Dotory" + "-" + uuid + "." + extension;
    }
}
