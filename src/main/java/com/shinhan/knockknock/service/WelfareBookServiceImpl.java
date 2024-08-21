package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.welfarebook.CreateWelfareBookRequest;
import com.shinhan.knockknock.domain.dto.welfarebook.ReadWelfareBookResponse;
import com.shinhan.knockknock.domain.entity.MatchEntity;
import com.shinhan.knockknock.domain.entity.UserEntity;
import com.shinhan.knockknock.domain.entity.WelfareBookEntity;
import com.shinhan.knockknock.domain.entity.WelfareEntity;
import com.shinhan.knockknock.repository.MatchRepository;
import com.shinhan.knockknock.repository.WelfareBookRepository;
import com.shinhan.knockknock.repository.UserRepository;
import com.shinhan.knockknock.repository.WelfareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class WelfareBookServiceImpl implements WelfareBookService {

    @Autowired
    WelfareBookRepository welfareBookRepo;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WelfareRepository welfareRepository;

    @Autowired
    MatchRepository matchRepository;

    @Override
    public Long createWelfareBook(CreateWelfareBookRequest request, Long userNo) {
        // 일반 사용자의 복지 서비스 신청 처리
        UserEntity user = userRepository.findById(userNo)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자가 존재하지 않습니다."));

        WelfareEntity welfare = welfareRepository.findByWelfareNameAndWelfarePrice(request.getWelfareName(), request.getWelfarePrice())
                .orElseThrow(() -> new NoSuchElementException("해당 복지 항목이 존재하지 않습니다."));

        WelfareBookEntity newWelfareBook = welfareBookRepo.save(dtoToEntity(request, user, welfare));
        return newWelfareBook.getWelfareBookNo();
    }

    @Override
    public Long createWelfareBookForProtege(CreateWelfareBookRequest request, Long protectorUserNo) {
        // 보호자가 매칭된 사용자를 위한 복지 서비스 예약 처리
        UserEntity protector = userRepository.findById(protectorUserNo)
                .orElseThrow(() -> new NoSuchElementException("해당 보호자가 존재하지 않습니다."));

        MatchEntity match = matchRepository.findByUserProtectorNoOrUserProtegeNo(protector, protector)
                .orElseThrow(() -> new NoSuchElementException("해당 보호자에게 매칭된 사용자가 없습니다."));

        UserEntity protege = match.getUserProtegeNo();  // 매칭된 일반 사용자

        WelfareEntity welfare = welfareRepository.findByWelfareNameAndWelfarePrice(request.getWelfareName(), request.getWelfarePrice())
                .orElseThrow(() -> new NoSuchElementException("해당 복지 항목이 존재하지 않습니다."));

        WelfareBookEntity newWelfareBook = welfareBookRepo.save(dtoToEntity(request, protege, welfare));
        return newWelfareBook.getWelfareBookNo();
    }

    @Override
    public List<ReadWelfareBookResponse> readAllByUserNo(Long userNo) {
        // userNo 별로 복지 예약 내역 조회
        List<WelfareBookEntity> entityList = welfareBookRepo.findByUser_UserNo(userNo);
        if (entityList.isEmpty()) {
            throw new NoSuchElementException("해당 사용자의 복지 예약 내역이 존재하지 않습니다.");
        }

        return entityList.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    @Override
    public ReadWelfareBookResponse readDetail(Long welfareBookNo) {
        WelfareBookEntity entity = welfareBookRepo.findById(welfareBookNo)
                .orElseThrow(() -> new NoSuchElementException("해당 복지 예약 내역이 존재하지 않습니다."));
        return entityToDto(entity);
    }

    @Override
    public void deleteWelfareBook(Long welfareBookNo) {
        if (!welfareBookRepo.existsById(welfareBookNo)) {
            throw new NoSuchElementException("해당 복지 예약 내역이 존재하지 않습니다.");
        }
        welfareBookRepo.deleteById(welfareBookNo);
    }
}

