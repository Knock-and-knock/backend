package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.auth.JwtProvider;
import com.shinhan.knockknock.domain.dto.consumption.ReadConsumptionRequest;
import com.shinhan.knockknock.domain.dto.consumption.ReadConsumptionResponse;
import com.shinhan.knockknock.service.consumption.ConsumptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/v1/consumption")
@RequiredArgsConstructor
@Tag(name = "소비리포트", description = "소비리포트 API")
public class ConsumptionController {
    private final ConsumptionService consumptionService;
    private final JwtProvider jwtProvider;

    @Operation(summary = "본인 소비 리포트 조회", description="userNo로 카드번호를 찾고 해당 카드 번호의 조회하고자 하는 월의 소비리포트 조회")
    @GetMapping
    public List<ReadConsumptionResponse> ReadConsumption(@RequestHeader("Authorization") String header , ReadConsumptionRequest readConsumptionRequest){
        Long userNo = jwtProvider.getUserNoFromHeader(header);
        return consumptionService.readConsumptionReport(userNo, readConsumptionRequest);
    }
}
