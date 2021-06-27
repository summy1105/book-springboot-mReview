package com.example.mreview.service;

import com.example.mreview.dto.ReviewDTO;
import com.example.mreview.entity.Member;
import com.example.mreview.entity.Movie;
import com.example.mreview.entity.Review;

import java.util.List;

public interface ReviewService {

    // 해당 영화의 모든 리뷰를 가져옴
    List<ReviewDTO> getListOfMovie(Long mno);
    
    // 리뷰 등록
    Long register(ReviewDTO movieReviewDTO);

    // 리뷰 수정
    void modify(ReviewDTO movieReviewDTO);

    // 리뷰 삭제
    void remove(Long reviewnum);

    default Review dtoToEntity(ReviewDTO movieReviewDTO){

        Movie movie = Movie.builder().mno(movieReviewDTO.getMno()).build();
        Member member = Member.builder().mid(movieReviewDTO.getMid()).build();

        Review review = Review.builder()
                .reviewNum(movieReviewDTO.getReviewnum())
                .movie(movie)
                .member(member)
                .grade(movieReviewDTO.getGrade())
                .text(movieReviewDTO.getText())
                .build();

        return review;
    }

    default ReviewDTO entityToDTO(Review movieReview){

        ReviewDTO movieReviewDTO = ReviewDTO.builder()
                .reviewnum(movieReview.getReviewNum())
                .mno(movieReview.getMovie().getMno())
                .mid(movieReview.getMember().getMid())
                .nickname(movieReview.getMember().getNickname())
                .email(movieReview.getMember().getEmail())
                .grade(movieReview.getGrade())
                .text(movieReview.getText())
                .regDate(movieReview.getRegDate())
                .modDate(movieReview.getModDate())
                .build();

        return movieReviewDTO;
    }
}
