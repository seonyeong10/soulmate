package com.soulmate.web.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ResultResDto {
    private int status;

    private String message;

    private String errorDescription = "";

    @Builder
    public ResultResDto(int status, String message, String errorDescription) {
        this.message = message;
        this.status = status;
        this.errorDescription = errorDescription;
    }
}
