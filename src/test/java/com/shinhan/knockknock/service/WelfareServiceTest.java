package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.welfare.CreateWelfareRequest;
import com.shinhan.knockknock.domain.dto.welfare.ReadWelfareResponse;
import com.shinhan.knockknock.domain.entity.WelfareEntity;
import com.shinhan.knockknock.repository.WelfareRepository;
import com.shinhan.knockknock.service.welfare.WelfareService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class WelfareServiceTest {

    @Autowired
    private WelfareService welfareService;

    @Autowired
    private WelfareRepository welfareRepository;

    @Test
    @DisplayName("복지 항목 생성 테스트")
    public void testCreateWelfare() {
        // Given
        CreateWelfareRequest request = CreateWelfareRequest.builder()
                .welfareName("건강 보험")
                .welfarePirce(1000L)
                .welfareCategory("건강")
                .build();

        // When
        Long welfareNo = welfareService.createWelfare(request);

        // Then
        WelfareEntity savedEntity = welfareRepository.findById(welfareNo).orElse(null);
        assertThat(savedEntity).isNotNull();
        assertThat(savedEntity.getWelfareName()).isEqualTo("건강 보험");
        assertThat(savedEntity.getWelfarePrice()).isEqualTo(1000L);
        assertThat(savedEntity.getWelfareCategory()).isEqualTo("건강");
    }

    @Test
    @DisplayName("복지 항목 조회 테스트")
    public void testReadAllWelfare() {
        // Given
        CreateWelfareRequest request = CreateWelfareRequest.builder()
                .welfareName("퇴직 연금")
                .welfarePirce(5000L)
                .welfareCategory("퇴직")
                .build();
        Long welfareNo = welfareService.createWelfare(request);

        // When
        List<ReadWelfareResponse> welfareList = welfareService.readAll();

        // Then
        assertThat(welfareList).hasSize(4);
        ReadWelfareResponse response = welfareList.get(0);
        assertThat(response.getWelfareName()).isEqualTo("일상가사");
        assertThat(response.getWelfarePirce()).isEqualTo(10000L);
        assertThat(response.getWelfareCategory()).isEqualTo("퇴직");
    }

    @Test
    @DisplayName("복지 항목 수정 테스트")
    public void testUpdateWelfare() {
        // Given
        CreateWelfareRequest createRequest = CreateWelfareRequest.builder()
                .welfareName("연금 계획")
                .welfarePirce(2000L)
                .welfareCategory("연금")
                .build();
        Long welfareNo = welfareService.createWelfare(createRequest);

        // When
        CreateWelfareRequest updateRequest = CreateWelfareRequest.builder()
                .welfareNo(welfareNo)
                .welfareName("수정된 연금 계획")
                .welfarePirce(2500L)
                .welfareCategory("수정된 연금")
                .build();
        welfareService.updateWelfare(updateRequest);

        // Then
        WelfareEntity updatedEntity = welfareRepository.findById(welfareNo).orElse(null);
        assertThat(updatedEntity).isNotNull();
        assertThat(updatedEntity.getWelfareName()).isEqualTo("수정된 연금 계획");
        assertThat(updatedEntity.getWelfarePrice()).isEqualTo(2500L);
        assertThat(updatedEntity.getWelfareCategory()).isEqualTo("수정된 연금");
    }

    @Test
    @DisplayName("복지 항목 삭제 테스트")
    public void testDeleteWelfare() {
        // Given
        CreateWelfareRequest request = CreateWelfareRequest.builder()
                .welfareName("장학금")
                .welfarePirce(3000L)
                .welfareCategory("교육")
                .build();
        Long welfareNo = welfareService.createWelfare(request);

        // When
        welfareService.deleteWelfare(welfareNo);

        // Then
        WelfareEntity deletedEntity = welfareRepository.findById(welfareNo).orElse(null);
        assertThat(deletedEntity).isNull();
    }
}
