package com.example.mreview.controller;

import com.example.mreview.dto.MovieDTO;
import com.example.mreview.dto.PageRequestDTO;
import com.example.mreview.dto.PageResultDTO;
import com.example.mreview.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/movie")
@Log4j2
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/register")
    public void register(){}

    @PostMapping("/register")
    public String register(MovieDTO movieDTO, RedirectAttributes redirectAttributes){
        log.info("register post...............................");
        System.out.println("movieDTO : "+ movieDTO);
        Long mno = movieService.register(movieDTO);

        redirectAttributes.addFlashAttribute("msg", mno);

        return "redirect:/movie/list";
    }

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        log.info("list get.....................................");
        System.out.println("pageRequestDTO: "+pageRequestDTO);

        PageResultDTO pageResultDTO = movieService.getList(pageRequestDTO);

        model.addAttribute("result", pageResultDTO);
    }

    @GetMapping({"/read","/modify"})
    public void read(long mno, @ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO,
                     Model model){
        log.info("read or modify get.........................");

        MovieDTO movieDTO = movieService.getMovie(mno);

        model.addAttribute("dto", movieDTO);
    }
}
