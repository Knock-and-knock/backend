package com.shinhan.knockknock.service.cardhistory;

import com.shinhan.knockknock.domain.dto.cardhistory.CreateCardHistoryRequest;
import com.shinhan.knockknock.domain.dto.cardhistory.ReadCardHistoryResponse;
import com.shinhan.knockknock.domain.entity.CardEntity;
import com.shinhan.knockknock.domain.entity.CardHistoryEntity;

import java.util.List;

public interface CardHistoryService {
    // 카드 내역 생성 메서드
    Long createCardHistory(CreateCardHistoryRequest request);

    // 모든 카드 내역 조회 메서드
    List<ReadCardHistoryResponse> readAll(Long cardId);

    // 가족 카드의 관련 사용자의 이름을 찾는 메서드
    String findUserNameForFamilyCard(CardEntity card);

    // DTO를 엔티티로 변환하는 기본 메서드
    CardHistoryEntity dtoToEntity(CreateCardHistoryRequest request);

    // 엔티티를 DTO로 변환하는 기본 메서드
    ReadCardHistoryResponse entityToDto(CardHistoryEntity entity, CardEntity card);
}
