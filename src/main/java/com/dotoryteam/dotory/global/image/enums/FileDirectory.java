package com.dotoryteam.dotory.global.image.enums;

import com.dotoryteam.dotory.global.image.exception.ExtensionEmptyException;
import com.dotoryteam.dotory.global.image.exception.InvalidDirectoryException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum FileDirectory {

    PROFILE("profile", Duration.ofMinutes(3) , 3 * 1024 * 1024L),
    CHAT("chat", Duration.ofMinutes(10) , 10 * 1024 * 1024L);

    private final String prefix;
    private final Duration uploadTime;
    private final long fileSize;

    public static FileDirectory of(String inputPrefix) {
        if (inputPrefix == null || inputPrefix.isEmpty()) {
            throw new ExtensionEmptyException();
        }
        return Arrays.stream(values())
                .filter(dir -> dir.prefix.equalsIgnoreCase(inputPrefix))
                .findFirst()
                .orElseThrow(InvalidDirectoryException::new);
    }
}
