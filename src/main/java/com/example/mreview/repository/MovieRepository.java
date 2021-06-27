package com.example.mreview.repository;

import com.example.mreview.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    // coalesce(...) : 안에 파라미터중에 null아닌 첫번째 값을 반환, 끝에 모두 null일 경우 대체할 값을 넣음
    @Query("select m, mi, avg(coalesce(r.grade, 0)), count(distinct r) from Movie m " +
            "left outer join MovieImage mi on mi.movie = m " +
            "left outer join Review r on r.movie = m group by m")
    Page<Object[]> getListPage(Pageable pageable);
    
    @Query("select m, mi, avg(coalesce(r.grade, 0)), count(r) " +
            " from Movie m left outer join MovieImage mi on mi.movie = m " +
            " left outer join Review r on r.movie = m " +
            " where m.mno = :mno group by mi")
    List<Object[]> getMovieWithAll(Long mno); //특정 영화 조회(영화, 이미지, 리뷰평균&갯수)
}
