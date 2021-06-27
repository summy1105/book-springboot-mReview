package com.example.mreview.service;

import com.example.mreview.dto.MovieDTO;
import com.example.mreview.dto.MovieImageDTO;
import com.example.mreview.dto.PageRequestDTO;
import com.example.mreview.dto.PageResultDTO;
import com.example.mreview.entity.Movie;
import com.example.mreview.entity.MovieImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface MovieService {
    Long register(MovieDTO movieDTO);

    PageResultDTO<MovieDTO, Object[]> getList(PageRequestDTO requestDTO);

    MovieDTO getMovie(Long mno);

    default MovieDTO entitiesToDTO(Movie movie, List<MovieImage> movieImages
            , Double avg, Long reviewCnt){

        List<MovieImageDTO> list = movieImages.stream().map(movieImage -> {
            return MovieImageDTO.builder()
                    .path(movieImage.getPath())
                    .imgName(movieImage.getImgName())
                    .uuid(movieImage.getUuid())
                    .build();
        }).collect(Collectors.toList());

        MovieDTO movieDTO = MovieDTO.builder()
                .mno(movie.getMno())
                .title(movie.getTitle())
                .regDate(movie.getRegDate())
                .modDate(movie.getModDate())
                .imageDTOList(list)
                .avg(avg)
                .reviewCnt(reviewCnt.intValue())
                .build();

        return movieDTO;
    }

    //map타입으로 반환
    default Map<String, Object> dtoToEntity(MovieDTO movieDTO){
        Map<String, Object> entityMap = new HashMap<>();

        Movie movie = Movie.builder()
                .mno(movieDTO.getMno())
                .title(movieDTO.getTitle())
                .build();

        entityMap.put("movie", movie);

        List<MovieImageDTO> imageDTOList = movieDTO.getImageDTOList();

        //MovieImageDTO 처리
        if(imageDTOList != null && imageDTOList.size()>0) {
            List<MovieImage> movieImagesList = imageDTOList.stream().map(movieImageDTO -> {
                MovieImage movieImage = MovieImage.builder()
                        .path(movieImageDTO.getPath())
                        .imgName(movieImageDTO.getImgName())
                        .uuid(movieImageDTO.getUuid())
                        .movie(movie)
                        .build();
                return movieImage;
            }).collect(Collectors.toList());

            entityMap.put("imgList", movieImagesList);
        }

        return entityMap;
    }
}
