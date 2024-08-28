package com.shinhan.knockknock.service.consumption;

import com.shinhan.knockknock.domain.dto.consumption.ReadConsumptionResponse;
import com.shinhan.knockknock.domain.entity.CardEntity;

import java.sql.Date;
import java.util.List;

public interface ConsumptionService {
    public List<String> readCardNoByUserNo(Long userNo);
    public List<CardEntity> readCardByUserNo(Long userNo);
    public List<ReadConsumptionResponse> readConsumptionReport(Long userNo, Date startDate, Date endDate);
    public List<ReadConsumptionResponse> readConsumptionReportForConversation(Long userNo, Date currentDate);
}
