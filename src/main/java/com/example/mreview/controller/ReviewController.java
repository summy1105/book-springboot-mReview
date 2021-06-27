package com.example.mreview.controller;

import com.example.mreview.dto.ReviewDTO;
import com.example.mreview.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@Log4j2
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/{mno}/all")
    public ResponseEntity<List<ReviewDTO>> getList(@PathVariable("mno") Long mno){
        log.info("review get list...................................");
        System.out.println("mno: "+mno);

        List<ReviewDTO> reviewDTOList = reviewService.getListOfMovie(mno);

        return new ResponseEntity<>(reviewDTOList, HttpStatus.OK);
    }

    @PostMapping("/{mno}")
    public ResponseEntity<Long> addReview(@RequestBody ReviewDTO reviewDTO){
        log.info("review post add....................................");
        System.out.println("reviewDTO: "+reviewDTO);

        Long reviewNum = reviewService.register(reviewDTO);

        return new ResponseEntity<>(reviewNum, HttpStatus.OK);
    }

    @PutMapping("/{mno}/{reviewnum}")
    public ResponseEntity<Long> modifyReview(@PathVariable Long reviewnum, @RequestBody ReviewDTO reviewDTO){
        log.info("review put modify...................................");
        System.out.println("reviewDTO: "+ reviewDTO);

        reviewService.modify(reviewDTO);

        return new ResponseEntity<>(reviewnum, HttpStatus.OK);
    }

    @DeleteMapping("/{mno}/{reviewnum}")
    public ResponseEntity<Long> removeReview(@PathVariable Long reviewnum){
        log.info("review delete remove........................");
        System.out.println("reviewnum: "+ reviewnum);

        reviewService.remove(reviewnum);

        return new ResponseEntity<>(reviewnum, HttpStatus.OK);
    }
}
