package com.dotoryteam.dotory.global.common.utils;

import com.dotoryteam.dotory.global.common.enums.AcceptableFileType;
import com.dotoryteam.dotory.global.image.exception.InvalidExtensionException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class GlobalFileValidator {
    public AcceptableFileType validateAndGetExtension(String fileName) {
        String extension = extractExtension(fileName);

        AcceptableFileType fileType = AcceptableFileType.fromExtension(extension);
        if (fileType == null)
            throw new InvalidExtensionException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일입니다.");

        return fileType;
    }

    private String extractExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            throw new InvalidExtensionException(HttpStatus.BAD_REQUEST , "잘못된 형식의 파일입니다.");
        }

        return fileName.substring(lastDotIndex + 1);
    }
}
