package com.dotoryteam.dotory.global.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
//커서랑 hasNext 값을 넣은 반환 형태가 필요한 경우 사용할 틀
public class CursorResult<T> {
    private List<T> content;
    private Boolean hasNext;
    private String nextCursorId;
}
