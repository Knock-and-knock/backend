package com.shinhan.knockknock.restcontroller;

import com.shinhan.knockknock.domain.dto.CreateWelfareRequest;
import com.shinhan.knockknock.repository.WelfareRepository;
import com.shinhan.knockknock.service.WelfareService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/Welfare")
@RequiredArgsConstructor
public class WelfareRestController {


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
}
