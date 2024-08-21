package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.welfarebook.CreateWelfareBookRequest;
import com.shinhan.knockknock.domain.dto.welfarebook.ReadWelfareBookResponse;
import com.shinhan.knockknock.domain.entity.UserEntity;
import com.shinhan.knockknock.domain.entity.WelfareBookEntity;
import com.shinhan.knockknock.domain.entity.WelfareEntity;
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

    @Override
    public Long createWelfareBook(CreateWelfareBookRequest request, Long userNo) {
        // UserEntity와 WelfareEntity를 조회하여 엔티티에 설정
        UserEntity user = userRepository.findById(userNo)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자가 존재하지 않습니다."));

        // 사용자 정보 체크
        if (user.getUserBirth() == null || user.getUserAddress() == null || user.getUserGender() == 0
                || user.getUserHeight() == 0 || user.getUserWeight() == 0 || user.getUserDisease() == null) {
            throw new NoSuchElementException("사용자 정보가 완전하지 않습니다.");
        }

        WelfareEntity welfare = welfareRepository.findByWelfareNameAndWelfarePrice(request.getWelfareName(), request.getWelfarePrice())
                .orElseThrow(() -> new NoSuchElementException("해당 복지 항목이 존재하지 않습니다."));

        WelfareBookEntity newWelfareBook = welfareBookRepo.save(dtoToEntity(request, user, welfare));
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
