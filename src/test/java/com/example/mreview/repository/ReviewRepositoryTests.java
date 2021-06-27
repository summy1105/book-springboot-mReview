package com.example.mreview.repository;

import com.example.mreview.entity.Member;
import com.example.mreview.entity.Movie;
import com.example.mreview.entity.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class ReviewRepositoryTests {
    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void insertMovieReview(){
        //200개 리뷰 등록
        IntStream.rangeClosed(1, 200).forEach(i->{
            //영화번호
            Long mno = (long)(Math.random()*100)+1;
            Movie movie = Movie.builder().mno(mno).build();

            //리뷰어번호
            Long mid = ((long)(Math.random()*100)+1);
            Member member = Member.builder().mid(mid).build();

            //평점 grade
            int grade = (int)(Math.random()*5)+1;

            Review review = Review.builder()
                    .member(member)
                    .movie(movie)
                    .grade(grade)
                    .text("review text..."+i)
                    .build();

            reviewRepository.save(review);
        });
    }

    @Test
    public void testGetMovieReviews(){
        Movie movie = Movie.builder().mno(9L).build();

        List<Review> result = reviewRepository.findByMovie(movie);

        for (Review review : result) {
            System.out.println(review+"  "+review.getMember().getEmail());
        }
    }
}
