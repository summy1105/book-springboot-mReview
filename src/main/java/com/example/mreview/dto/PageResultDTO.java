package com.example.mreview.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResultDTO<DTO,ENTITY> {
    //DTO List
    private List<DTO> dtoList;

    //total page
    private int totalPage;

    //present page number
    private int page;
    //size of a page
    private int size;

    // start, end page number
    private int start, end;

    private boolean prev, next;

    private List<Integer> pageList;

    public PageResultDTO(Page<ENTITY> result, Function<ENTITY, DTO> fn){
        dtoList = result.stream().map(fn).collect(Collectors.toList());

        totalPage = result.getTotalPages();
        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable){
        this.page = pageable.getPageNumber()+1; // 0부터 시작
        this.size = pageable.getPageSize(); //한페이지에 보일 데이터 갯스
        //temp end page 현재 페이지의 끝번호
        int tempEnd = (int)(Math.ceil(page/10.0))*10; //클릭할 페이지 번호 갯수 1~10 게시글수가 아님

        start = tempEnd -9; //현재 view의 페이지 시작
        prev = start > 1;

        end = totalPage > tempEnd ? tempEnd : totalPage; //현재 view의 페이지 끝번호
        next = totalPage > tempEnd;

        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
        //boxed() ->int, long, double을 Integer, Long, Double로 바꿔줌
    }
}
