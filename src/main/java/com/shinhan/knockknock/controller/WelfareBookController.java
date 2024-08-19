package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.domain.dto.CreateWelfareBookRequest;
import com.shinhan.knockknock.repository.WelfareBookRepository;
import com.shinhan.knockknock.service.WelfareBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/welfare-book")
@RequiredArgsConstructor
@Tag(name = "복지 예약 내역", description = "복지 예약 목록 API")
public class WelfareBookController {

    final WelfareBookRepository welfareBookRepo;
    final WelfareBookService welfareBookService;

    @Operation(summary = "복지 예약 전체 조회", description = "복지 예약 내역을 전부 조회하는 API입니다.")
    @GetMapping
    void readAll(Model model){
        model.addAttribute("welfareBook", welfareBookRepo.findAll());
    }

    @Operation(summary = "복지 예약 조회 detail", description = "복지 예약 내역 중 하나를 조회하는 API입니다.")
    @GetMapping("/{welfareBookNo}")
    void readDetail(@PathVariable("welfareBookNo") Long welfareBookNo, Model model){
        model.addAttribute("welfareBookDetail", welfareBookRepo.findById(welfareBookNo));
    }

    @Operation(summary = "복지 예약 하기" , description = "복지서비스를 예약하는 API입니다.")
    @PostMapping
    Long create(@RequestBody CreateWelfareBookRequest request){
        return welfareBookService.createWelfareBook(request);
    }

    @Operation(summary = "복지 예약 취소", description = "복지서비스 예약을 취소하는 API입니다.")
    @DeleteMapping("/{welfareBookNo}")
    void delete(@PathVariable("welfareBookNo") Long welfareBookNo){
        welfareBookService.deleteWelfareBook(welfareBookNo);
    }
}
