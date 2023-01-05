package com.hyeon.noticeboard.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    // 로그인
    public Long login(String id, String pwd) {

        Member member = memberRepository.findById(id).get(0);
        if (member.getPwd().equals(pwd)) {
            return member.getMemberId();
        }
        return Long.parseLong("-1"); // 로그인 실패
    }

    // 회원가입
    @Transactional
    public Long join(String id, String pwd, String name) {
        //validateDuplicateMember(member); // 중복 회원 검증 제외
        Member member = Member.builder()
                .id(id)
                .pwd(pwd)
                .name(name)
                .build();
        return memberRepository.save(member);
    }


    // 전체 회원 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // 회원 찾기
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }


}