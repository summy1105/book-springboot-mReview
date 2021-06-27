package com.example.mreview.repository;

import com.example.mreview.entity.Member;
import com.example.mreview.entity.Movie;
import com.example.mreview.entity.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    //attributePaths : 로딩설정을 변경하고 싶은 속성의 이름을 배열로 명시
    //type : @EntityGraph를 어떤 방식으로 적용할 것인지 설정
    // (FETCH- 명시속성 EAGER, else LAZY, LOAD - 명시속성 EAGER, else 기존 방식)
    @EntityGraph(attributePaths = {"member"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Review> findByMovie(Movie movie);// 메소드 이름으로 쿼리 생성됨

    // update나 delete를 이용하기 위해서 @Modifying 어노테이션 필요
    @Modifying
    @Query("delete from Review mr where mr.member = :member")
    void deleteByMember(Member member); //단순히 메소드 이름으로 쿼리를 생성하면 review에서 한개씩 삭제됨
}
