package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.domain.dto.CreateCardCategoryRequest;
import com.shinhan.knockknock.domain.dto.CreateCardHistoryRequest;
import com.shinhan.knockknock.repository.CardHistoryRepository;
import com.shinhan.knockknock.service.CardCategoryService;
import com.shinhan.knockknock.service.CardHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/card-history")
@RequiredArgsConstructor
@Tag(name = "CardHistory", description = "카드 내역 목록 API")
public class CardHistoryController {

    final CardHistoryRepository cardHistoryRepo;
    final CardHistoryService cardHistoryService;
    final CardCategoryService cardCategoryService;

    @Operation(summary = "카드 내역 전체 조회")
    @GetMapping("/used")
    void readAll(Model model){
        model.addAttribute("cardHistory", cardHistoryRepo.findAll());
    }

    @Operation(summary = "카드 내역 생성")
    @PostMapping("/used")
    Long create(@RequestBody CreateCardHistoryRequest request){
        return cardHistoryService.createCardHistory(request);
    }

    @Operation(summary = "카드 내역 카테고리 수정")
    @PutMapping(value = "/used", consumes = "application/json;charset=utf-8", produces = "text/plain;charset=utf-8")
    void update(@RequestBody CreateCardCategoryRequest request){
        cardCategoryService.updateCardCategory(request);
    }
}
