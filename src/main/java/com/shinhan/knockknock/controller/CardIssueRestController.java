package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.domain.dto.CreateCardIssueRequest;
import com.shinhan.knockknock.domain.dto.CreateCardResponse;
import com.shinhan.knockknock.service.CardIssueServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/card")
public class CardIssueRestController {

    @Autowired
    CardIssueServiceImpl cardIssueService;

    // 카드 발급
    @PostMapping("/apply")
    public CreateCardResponse createCardIssue(@RequestBody CreateCardIssueRequest request) {
        CreateCardResponse createCardResponse = cardIssueService.createPostCardIssue(request);
        return createCardResponse;
    }


}
