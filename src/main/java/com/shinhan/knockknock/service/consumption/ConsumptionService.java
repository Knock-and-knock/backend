package com.shinhan.knockknock.service.consumption;

import com.shinhan.knockknock.domain.dto.consumption.ReadConsumptionResponse;

import java.sql.Date;
import java.util.List;

public interface ConsumptionService {
    public List<String> readCardNoByUserNo(Long userNo);
    public List<ReadConsumptionResponse> readConsumptionReport(Long userNo, Date startDate, Date endDate);
}
