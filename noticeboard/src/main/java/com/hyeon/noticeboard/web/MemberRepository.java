package com.hyeon.noticeboard.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor // 생성자 주입
public class MemberRepository {

    private final EntityManager em;
    //private final JPAFactoryQuery query;

    public Long save(Member member) {
        if (member.getMemberId() == null) {
            em.persist(member);
        } else {
            em.merge(member); //  detached 된 상태 -> persist 상태
        }
        return member.getMemberId();
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findById(String id) {
        return em.createQuery("select i from Member i where i.id =:id", Member.class)
                .setParameter("id", id)
                .getResultList();
    }

/*    public List<Member> findById(String id) {
        return queryFactory
                .selectFrom(member)
                .where(member.id.eq(id))
                .fetchOne();
    }*/


    public List<Member> findAll() {
        return em.createQuery("select i from Member i",Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select i from Member i where i.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
    

}