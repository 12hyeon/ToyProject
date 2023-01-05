package com.hyeon.noticeboard.web;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    private String name;

    private String id;
    private String pwd;

    private Integer age;

    //@Temporal(TemporalType.TIMESTAMP) // 날짜와 시간 타입 매핑
    private Date createdDate;

    //@Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;


    @Builder
    private Member(String id, String pwd, String name) {
        this.id = id;
        this.pwd = pwd;
        this.name = name;

        this.createdDate = new Date();
        this.lastModifiedDate = new Date();
    }

}
