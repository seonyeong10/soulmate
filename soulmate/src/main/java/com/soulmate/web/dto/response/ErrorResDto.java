package com.soulmate.web.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResDto {
    private String objectName;

    private String field;

    private String message;
}
