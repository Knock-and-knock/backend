package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.CreateCardHistoryRequest;
import com.shinhan.knockknock.domain.dto.ReadCardHistoryResponse;
import com.shinhan.knockknock.domain.entity.CardEntity;
import com.shinhan.knockknock.domain.entity.CardHistoryEntity;
import com.shinhan.knockknock.domain.entity.UserEntity;
import com.shinhan.knockknock.repository.CardHistoryRepository;
import com.shinhan.knockknock.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
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
    public List<ReadCardHistoryResponse> readAll() {
        try {
            List<CardHistoryEntity> entityList = cardHistoryRepo.findAll();
            if (entityList.isEmpty()) {
                throw new NoSuchElementException("카드 내역이 존재하지 않습니다.");
            }

            return entityList.stream().map(entity -> {
                UserEntity user = userRepository.findById(entity.getCardId().getUserNo())
                        .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));
                return entityToDto(entity, user);
            }).collect(Collectors.toList());

        } catch (NoSuchElementException e) {
            throw e;  // 컨트롤러에서 처리
        } catch (Exception e) {
            throw new RuntimeException("카드 내역 조회에 실패했습니다.", e);
        }
    }

    @Override
    public String findUserNameForFamilyCard(CardEntity card) {
        // 카드가 가족 카드인 경우, 같은 은행과 계정을 가진 다른 사용자를 찾습니다.
        UserEntity relatedUser = userRepository.findByCards_CardBankAndCards_CardAccountAndCards_CardIsfamilyFalse(
                        card.getCardBank(), card.getCardAccount())
                .orElseThrow(() -> new NoSuchElementException("관련 사용자를 찾을 수 없습니다."));
        return relatedUser.getUserName();
    }

}
