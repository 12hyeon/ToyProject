package com.hyeon.noticeboard.kakao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class KakaoController {
    private final KakaoService ks;

    @RequestMapping("/login")
    public String loginPage()
    {
        return "login";
    }

    @GetMapping("/kakao") // front-end에서 kakao에서 보내는 code 받아서 처리
    public String getCI(@RequestParam String code, Model model) throws IOException {
        System.out.println("code = " + code);
        String access_token = ks.getToken(code);
        KakaoDTO userInfo = ks.getUserInfo(access_token);
        System.out.println(userInfo);

        // index.html 파일 형태로 전달 받은 값 확인
        model.addAttribute("code", code);
        model.addAttribute("access_token", access_token);
        model.addAttribute("userInfo", userInfo);

        //ci는 비즈니스 전환후 검수신청 -> 허락받아야 수집 가능
        return "index";
    }
}