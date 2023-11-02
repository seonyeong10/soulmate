package com.soulmate.web.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FileResDto {
    private int seq;

    private String originalName;

    private String savedName;
}
