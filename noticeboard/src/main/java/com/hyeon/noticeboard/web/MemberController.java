package com.hyeon.noticeboard.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@ResponseBody
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping(value = "/members/new")
    public String join(@RequestParam("id") String id,
                       @RequestParam("pwd") String pwd,
                       @RequestParam("name") String name) {


        Long result = memberService.join(id, pwd, name);
        if (result == Long.parseLong("-1")) {
            return "회원가입 실패!";
        }
        return "회원가입 성공 - id"+result;
    }

    // 로그인
    @GetMapping(value = "/members")
    public String login(@RequestParam String id, @RequestParam String pwd) {

        Long result = memberService.login(id, pwd);
        if (result == Long.parseLong("-1")) {
            return "로그인 실패!";
        }
        return "로그인 성공 - id"+result;
    }
}
