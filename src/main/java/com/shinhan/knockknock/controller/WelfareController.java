package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.domain.dto.WelfareResponse;
import com.shinhan.knockknock.repository.WelfareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/welfare")
@RequiredArgsConstructor
public class WelfareController {

    final WelfareRepository welfareRepo;

    @GetMapping("/test")
    void readAll(){
    }
}
