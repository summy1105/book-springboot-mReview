package com.example.mreview.service;

import com.example.mreview.dto.ReviewDTO;
import com.example.mreview.entity.Movie;
import com.example.mreview.entity.Review;
import com.example.mreview.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;

    @Override
    public List<ReviewDTO> getListOfMovie(Long mno) {
        Movie movie = Movie.builder().mno(mno).build();

        List<Review> result = reviewRepository.findByMovie(movie);

        return result.stream()
                .map(movieReview->entityToDTO(movieReview)).collect(Collectors.toList());
    }

    @Override
    public Long register(ReviewDTO movieReviewDTO) {
        Review review = dtoToEntity(movieReviewDTO);

        reviewRepository.save(review);

        return review.getReviewNum();
    }

    @Override
    public void modify(ReviewDTO movieReviewDTO) {
        Optional<Review> result =
                reviewRepository.findById(movieReviewDTO.getReviewnum());

        if(result.isPresent()){
            Review review = result.get();

            review.changeGrade(movieReviewDTO.getGrade());
            review.changeText(movieReviewDTO.getText());

            reviewRepository.save(review);
        }
    }

    @Override
    public void remove(Long reviewnum) {
        reviewRepository.deleteById(reviewnum);
    }
}
