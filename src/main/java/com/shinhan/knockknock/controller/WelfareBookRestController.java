package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.domain.dto.CreateWelfareBookRequest;
import com.shinhan.knockknock.domain.entity.WelfareBookEntity;
import com.shinhan.knockknock.repository.WelfareBookRepository;
import com.shinhan.knockknock.service.WelfareBookService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/welfare-book")
@RequiredArgsConstructor
public class WelfareBookRestController {

    final WelfareBookRepository welfareBookRepo;
    final WelfareBookService welfareBookService;

    @Operation(summary = "복지 예약 전체 조회")
    @GetMapping("/regist")
    void readAll(Model model){
        model.addAttribute("welfareBook", welfareBookRepo.findAll());
    }

    @Operation(summary = "복지 예약 조회 detail")
    @GetMapping("/regist/{welfareBookNo}")
    void readDetail(@PathVariable("welfareBookNo") Long welfareBookNo, Model model){
        model.addAttribute("welfareBookDetail", welfareBookRepo.findById(welfareBookNo));
    }

    @Operation(summary = "복지 예약 하기")
    @PostMapping("/regist")
    Long create(@RequestBody CreateWelfareBookRequest request){
        return welfareBookService.createWelfareBook(request);
    }

    @Operation(summary = "복지 예약 취소")
    @DeleteMapping("/regist/{welfareBookNo}")
    void delete(@PathVariable("welfareBookNo") Long welfareBookNo){
        welfareBookService.deleteWelfareBook(welfareBookNo);
    }
}
