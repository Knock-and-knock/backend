package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.CreateWelfareBookRequest;
import com.shinhan.knockknock.domain.dto.ReadWelfareBookResponse;
import com.shinhan.knockknock.domain.entity.WelfareBookEntity;
import com.shinhan.knockknock.repository.WelfareBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class WelfareBookServiceImpl implements WelfareBookService {

    @Autowired
    WelfareBookRepository welfareBookRepo;

    @Override
    public Long createWelfareBook(CreateWelfareBookRequest request) {
        WelfareBookEntity newWelfareBook = welfareBookRepo.save(dtoToEntity(request));
        return newWelfareBook.getWelfareBookNo();
    }

    @Override
    public List<ReadWelfareBookResponse> readAll() {
        List<WelfareBookEntity> entityList = welfareBookRepo.findAll();
        if (entityList.isEmpty()) {
            throw new NoSuchElementException("복지 예약 내역이 존재하지 않습니다.");
        }
        Function<WelfareBookEntity, ReadWelfareBookResponse> function = this::entityToDto;
        return entityList.stream().map(function).collect(Collectors.toList());
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
