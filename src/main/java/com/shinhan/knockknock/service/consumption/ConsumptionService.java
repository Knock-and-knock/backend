package com.shinhan.knockknock.service.consumption;

import com.shinhan.knockknock.domain.dto.consumption.ReadConsumptionResponse;
import com.shinhan.knockknock.domain.entity.CardEntity;

import java.sql.Date;
import java.util.List;

public interface ConsumptionService {
    public List<String> readCardNoByUserNo(Long userNo);

    public List<CardEntity> readCardByUserNo(Long userNo);
    public String readConsumptionReportForConversation(CardEntity card, String date);

    public List<ReadConsumptionResponse> readConsumptionReportList(Long userNo, Date startDate, Date endDate);
    public List<ReadConsumptionResponse> readConsumptionReport(Long cardId, Date startDate, Date endDate);

}
