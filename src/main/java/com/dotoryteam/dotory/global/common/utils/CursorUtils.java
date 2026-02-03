package com.dotoryteam.dotory.global.common.utils;

import com.dotoryteam.dotory.global.common.exception.InvalidCursorValueException;

import java.util.Base64;

public class CursorUtils {

    //ID 값 -> 커서 암호화
    public static String toCursor(Long id) {
        if (id == null) return null;
        return Base64.getEncoder().encodeToString(String.valueOf(id).getBytes());
    }

    // 암호화된 커서 -> ID 값
    public static Long parseCursor(String cursor) {
        if (cursor == null || cursor.isBlank()) return null;
        try {
            String decoded = new String(Base64.getDecoder().decode(cursor));
            return Long.parseLong(decoded);
        } catch (IllegalArgumentException e) {
            throw new InvalidCursorValueException();
        }
    }
}
