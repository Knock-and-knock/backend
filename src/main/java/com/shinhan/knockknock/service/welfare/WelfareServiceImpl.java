package com.shinhan.knockknock.service.welfare;

import com.shinhan.knockknock.domain.dto.welfare.CreateWelfareRequest;
import com.shinhan.knockknock.domain.dto.welfare.ReadWelfareResponse;
import com.shinhan.knockknock.domain.entity.WelfareEntity;
import com.shinhan.knockknock.repository.WelfareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class WelfareServiceImpl implements WelfareService{

    @Autowired
    WelfareRepository welfareRepo;

    @Override
    public Long createWelfare(CreateWelfareRequest request) {
        WelfareEntity newWelfare = welfareRepo.save(dtoToEntity(request));
        return newWelfare.getWelfareNo();
    }

    @Override
    public List<ReadWelfareResponse> readAll() {
        List<WelfareEntity> entityList = welfareRepo.findAll();
        if (entityList.isEmpty()) {
            throw new NoSuchElementException("복지 목록이 존재하지 않습니다.");
        }
        Function<WelfareEntity, ReadWelfareResponse> function = this::entityToDto;
        return entityList.stream().map(function).collect(Collectors.toList());
    }

    @Override
    public void updateWelfare(CreateWelfareRequest request) {
        WelfareEntity welfare = welfareRepo.findById(request.getWelfareNo())
                .orElseThrow(() -> new NoSuchElementException("해당 복지 서비스가 존재하지 않습니다."));
        welfare.setWelfareName(request.getWelfareName());
        welfare.setWelfarePrice(request.getWelfarePirce());
        welfare.setWelfareCategory(request.getWelfareCategory());
        welfareRepo.save(welfare);
    }

    @Override
    public void deleteWelfare(Long welfareNo) {
        if (!welfareRepo.existsById(welfareNo)) {
            throw new NoSuchElementException("해당 복지 서비스가 존재하지 않습니다.");
        }
        welfareRepo.deleteById(welfareNo);
    }
}
