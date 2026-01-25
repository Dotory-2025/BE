package com.dotoryteam.dotory.global.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum AcceptableFileType {
    JPG("jpg" , "image/jpg") ,
    JPEG("jpeg" , "image/jpeg") ,
    PNG("png" , "image/png");

    private final String extension;
    private final String mimeType;

    //해당 확장자가 있는지
    public static AcceptableFileType fromExtension(String extension) {
        return Arrays.stream(values())
                .filter(has -> has.extension.equalsIgnoreCase(extension))
                .findFirst()
                .orElse(null);
    }
}
