package com.dotoryteam.dotory.global.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Semester {
    SPRING("봄학기"),
    SUMMER("여름학기"),
    FALL("가을학기"),
    WINTER("겨울학기");

    private final String description;
}