package com.shinhan.knockknock.service.welfarebook;

import com.shinhan.knockknock.domain.dto.welfarebook.CreateWelfareBookRequest;
import com.shinhan.knockknock.domain.dto.welfarebook.ReadWelfareBookResponse;
import com.shinhan.knockknock.domain.entity.UserEntity;
import com.shinhan.knockknock.domain.entity.WelfareBookEntity;
import com.shinhan.knockknock.domain.entity.WelfareEntity;
import com.shinhan.knockknock.repository.MatchRepository;
import com.shinhan.knockknock.repository.UserRepository;
import com.shinhan.knockknock.repository.WelfareBookRepository;
import com.shinhan.knockknock.repository.WelfareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WelfareBookServiceImpl implements WelfareBookService {


    final WelfareBookRepository welfareBookRepo;
    final UserRepository userRepository;
    final WelfareRepository welfareRepository;
    final MatchRepository matchRepository;

    @Override
    public Long createWelfareBook(CreateWelfareBookRequest request, Long userNo) {
        // 일반 사용자의 복지 서비스 신청 또는 보호자가 매칭된 일반 사용자를 대신한 신청 처리
        UserEntity user = userRepository.findById(userNo)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자가 존재하지 않습니다."));

        WelfareEntity welfare = welfareRepository.findById(request.getWelfareNo())
                .orElseThrow(() -> new NoSuchElementException("해당 복지 항목이 존재하지 않습니다."));

        WelfareBookEntity newWelfareBook = welfareBookRepo.save(dtoToEntity(request, user, welfare));

        user.setUserAddress(request.getUserAddress());
        user.setUserHeight(request.getUserHeight());
        user.setUserBirth(request.getUserBirth());
        user.setUserDisease(request.getUserDisease());
        user.setUserGender(request.getUserGender());
        user.setUserWeight(request.getUserWeight());

        userRepository.save(user);

        return newWelfareBook.getWelfareBookNo();
    }

    // DTO -> Entity 변환
    private WelfareBookEntity dtoToEntity(CreateWelfareBookRequest request, UserEntity user, WelfareEntity welfare) {
        return WelfareBookEntity.builder()
                .welfareBookStartDate(request.getWelfareBookStartDate())
                .welfareBookEndDate(request.getWelfareBookEndDate())
                .welfareBookIsCansle(request.isWelfareBookIsCansle())
                .welfareBookIsComplete(request.isWelfareBookIsComplete())
                .user(user)
                .welfare(welfare)
                .welfareBookUseTime(request.getWelfareBookUseTime())  // welfareBookUseTime 저장
                .build();
    }

    @Override
    public List<ReadWelfareBookResponse> readAllByUserNo(Long userNo) {
        // userNo 별로 복지 예약 내역 조회
        List<WelfareBookEntity> entityList = welfareBookRepo.findByUser_UserNo(userNo);

        return entityList.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    @Override
    public ReadWelfareBookResponse readDetail(Long welfareBookNo) {
        WelfareBookEntity entity = welfareBookRepo.findById(welfareBookNo)
                .orElseThrow(() -> new NoSuchElementException("해당 복지 예약 내역이 존재하지 않습니다."));
        return entityToDto(entity);
    }

    @Override
    public List<ReadWelfareBookResponse> readAllByLastMonth(Long userNo) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneMonthAgo = now.minusMonths(1);

        Timestamp startDate = Timestamp.valueOf(oneMonthAgo);
        Timestamp endDate = Timestamp.valueOf(now);

        List<WelfareBookEntity> welfareBooks = welfareBookRepo.findByUser_UserNoAndWelfareBookStartDateBetween(userNo, startDate, endDate);
        return welfareBooks.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteWelfareBook(Long welfareBookNo) {
        if (!welfareBookRepo.existsById(welfareBookNo)) {
            throw new NoSuchElementException("해당 복지 예약 내역이 존재하지 않습니다.");
        }
        welfareBookRepo.deleteById(welfareBookNo);
    }
}


