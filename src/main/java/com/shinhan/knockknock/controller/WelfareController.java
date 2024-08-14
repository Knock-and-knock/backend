package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.domain.dto.CreateWelfareRequest;
import com.shinhan.knockknock.repository.WelfareRepository;
import com.shinhan.knockknock.service.WelfareService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/welfare")
@RequiredArgsConstructor
public class WelfareController {


    final WelfareRepository welfareRepo;
    final WelfareService welfareService;

    @Operation(summary = "복지 리스트 전체 조회")
    @GetMapping("/list")
    void readAll(Model model){
        model.addAttribute("welfare", welfareRepo.findAll());
    }

    @Operation(summary = "복지 생성")
    @PostMapping("/list")
    Long create(@RequestBody CreateWelfareRequest request){
        return welfareService.createWelfare(request);
    }

    @Operation(summary = "복지 수정")
    @PutMapping(value = "/list", consumes = "application/json;charset=utf-8", produces = "text/plain;charset=utf-8")
    void update(@RequestBody CreateWelfareRequest request){
        welfareService.updateWelfare(request);
    }

    @Operation(summary = "복지 삭제")
    @DeleteMapping("/list/{welfareNo}")
    void delete(@PathVariable("welfareNo") Long welfareNo){
        welfareService.deleteWelfare(welfareNo);
    }
}
