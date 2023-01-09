package com.hyeon.noticeboard.kakao;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.util.Date;

@Data
@Setter
public class KakaoDTO {

    private String name;
    private String email;

    private String id;
    // private String token;

    @Builder
    private KakaoDTO(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
