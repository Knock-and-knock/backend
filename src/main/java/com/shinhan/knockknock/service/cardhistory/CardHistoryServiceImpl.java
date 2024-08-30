package com.shinhan.knockknock.service.cardhistory;

import com.shinhan.knockknock.domain.dto.cardhistory.CreateCardHistoryRequest;
import com.shinhan.knockknock.domain.dto.cardhistory.ReadCardHistoryResponse;
import com.shinhan.knockknock.domain.entity.CardCategoryEntity;
import com.shinhan.knockknock.domain.entity.CardEntity;
import com.shinhan.knockknock.domain.entity.CardHistoryEntity;
import com.shinhan.knockknock.domain.entity.UserEntity;
import com.shinhan.knockknock.repository.CardHistoryRepository;
import com.shinhan.knockknock.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CardHistoryServiceImpl implements CardHistoryService {

    @Autowired
    private CardHistoryRepository cardHistoryRepo;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Long createCardHistory(CreateCardHistoryRequest request) {
        try {
            CardHistoryEntity newCardHistory = cardHistoryRepo.save(dtoToEntity(request));
            return newCardHistory.getCardHistoryNo();
        } catch (DataAccessException e) {
            throw new RuntimeException("카드 내역 생성 중 데이터베이스 오류가 발생했습니다.", e);
        } catch (Exception e) {
            throw new RuntimeException("카드 내역 생성에 실패했습니다.", e);
        }
    }

    @Override
    public List<ReadCardHistoryResponse> readAll(Long cardId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "cardHistoryApprove");

        try {
            List<CardHistoryEntity> entityList = cardHistoryRepo.findByCard_CardId(cardId, sort);
            return entityList.stream().map(this::entityToDto).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("카드 내역 조회에 실패했습니다.", e);
        }
    }

    @Override
    public List<ReadCardHistoryResponse> readAllWithinDateRange(Long cardId, Timestamp startDate, Timestamp endDate) {
        try {
            List<CardHistoryEntity> entityList = cardHistoryRepo.findByCard_CardIdAndCardHistoryApproveBetween(cardId, startDate, endDate);
            return entityList.stream().map(this::entityToDto).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("카드 내역 조회에 실패했습니다.", e);
        }
    }

    @Override
    public String findUserNameForFamilyCard(CardEntity card) {
        UserEntity relatedUser = userRepository.findByCards_CardBankAndCards_CardAccountAndCards_CardIsfamilyFalse(
                        card.getCardBank(), card.getCardAccount())
                .orElseThrow(() -> new NoSuchElementException("관련 사용자를 찾을 수 없습니다."));
        return relatedUser.getUserName();
    }

    @Override
    public CardHistoryEntity dtoToEntity(CreateCardHistoryRequest request) {
        return CardHistoryEntity.builder()
                .cardHistoryNo(request.getCardHistoryNo())
                .cardHistoryAmount(request.getCardHistoryAmount())
                .cardHistoryShopname(request.getCardHistoryShopname())
                .cardHistoryApprove(new Timestamp(System.currentTimeMillis())) // 예시로 현재 시간 사용
                .cardCategory(CardCategoryEntity.builder().cardCategoryNo(request.getCardCategoryNo()).build())
                .card(CardEntity.builder().cardId(request.getCardNo()).build())
                .build();
    }

    @Override
    public ReadCardHistoryResponse entityToDto(CardHistoryEntity entity) {
        return ReadCardHistoryResponse.builder()
                .cardHistoryAmount(entity.getCardHistoryAmount())
                .cardHistoryShopname(entity.getCardHistoryShopname())
                .cardHistoryApprove(entity.getCardHistoryApprove())
                .cardCategoryNo(entity.getCardCategoryNo())
                .cardNo(entity.getCardId())
                .cardBank(entity.getCardBank())
                .cardAccount(entity.getCardAccount())
                .isCardFamily(entity.isCardFamily())
                .cardHistoryIsCansle(entity.getCardHistoryIsCansle() != null && entity.getCardHistoryIsCansle())
                .build();
    }
}