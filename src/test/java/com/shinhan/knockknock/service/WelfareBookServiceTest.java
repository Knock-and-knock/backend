package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.CreateWelfareBookRequest;
import com.shinhan.knockknock.domain.dto.ReadWelfareBookResponse;
import com.shinhan.knockknock.domain.entity.WelfareBookEntity;
import com.shinhan.knockknock.repository.WelfareBookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class WelfareBookServiceTest {

    @Autowired
    private WelfareBookService welfareBookService;

    @Autowired
    private WelfareBookRepository welfareBookRepository;

    @Test
    @DisplayName("복지 예약 생성 테스트")
    public void testCreateWelfareBook() {
        // Given
        CreateWelfareBookRequest request = CreateWelfareBookRequest.builder()
                .userNo(1)
                .welfareBookStartDate(Timestamp.valueOf("2024-08-01 10:00:00"))
                .welfareBookEndDate(Timestamp.valueOf("2024-08-31 18:00:00"))
                .welfareBookIsCansle(false)
                .welfareBookIsComplete(false)
                .welfareNo(1L)  // welfareNo 설정
                .build();

        // When
        Long welfareBookNo = welfareBookService.createWelfareBook(request);

        // Then
        WelfareBookEntity savedEntity = welfareBookRepository.findById(welfareBookNo).orElse(null);
        assertThat(savedEntity).isNotNull();
        assertThat(savedEntity.getUserId()).isEqualTo("user123");
        assertThat(savedEntity.getWelfareBookStartDate()).isEqualTo(Timestamp.valueOf("2024-08-01 10:00:00"));
        assertThat(savedEntity.getWelfareBookEndDate()).isEqualTo(Timestamp.valueOf("2024-08-31 18:00:00"));
        assertThat(savedEntity.isWelfareBookIsCansle()).isFalse();
        assertThat(savedEntity.isWelfareBookIsComplete()).isFalse();
        assertThat(savedEntity.getWelfareBookNo()).isEqualTo(welfareBookNo);  // 자동 생성된 welfareBookNo 확인
        assertThat(savedEntity.getWelfareNo()).isEqualTo(1L);  // welfareNo 확인
    }

    @Test
    @DisplayName("복지 예약 전체 조회 테스트")
    public void testReadAllWelfareBooks() {
        // Given
        CreateWelfareBookRequest request1 = CreateWelfareBookRequest.builder()
                .userId("user123")
                .welfareBookStartDate(Timestamp.valueOf("2024-08-01 10:00:00"))
                .welfareBookEndDate(Timestamp.valueOf("2024-08-31 18:00:00"))
                .welfareBookIsCansle(false)
                .welfareBookIsComplete(false)
                .welfareNo(1L)
                .build();

        CreateWelfareBookRequest request2 = CreateWelfareBookRequest.builder()
                .userId("user456")
                .welfareBookStartDate(Timestamp.valueOf("2024-09-01 10:00:00"))
                .welfareBookEndDate(Timestamp.valueOf("2024-09-30 18:00:00"))
                .welfareBookIsCansle(false)
                .welfareBookIsComplete(false)
                .welfareNo(2L)
                .build();

        welfareBookService.createWelfareBook(request1);
        welfareBookService.createWelfareBook(request2);

        // 데이터베이스에 저장된 엔티티를 직접 확인
        assertThat(welfareBookRepository.findAll()).hasSize(2);

        // When
        List<ReadWelfareBookResponse> welfareBookList = welfareBookService.readAll();  // welfareNo로 조회

        // Then
        assertThat(welfareBookList).hasSize(2);
    }

    @Test
    @DisplayName("복지 예약 상세 조회 테스트")
    public void testReadDetailWelfareBook() {
        // Given
        CreateWelfareBookRequest request = CreateWelfareBookRequest.builder()
                .userId("user123")
                .welfareBookStartDate(Timestamp.valueOf("2024-08-01 10:00:00"))
                .welfareBookEndDate(Timestamp.valueOf("2024-08-31 18:00:00"))
                .welfareBookIsCansle(false)
                .welfareBookIsComplete(false)
                .welfareNo(1L)
                .build();
        Long welfareBookNo = welfareBookService.createWelfareBook(request);

        // When
        ReadWelfareBookResponse response = welfareBookService.readDetail(welfareBookNo);  // welfareBookNo로 상세 조회

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo("user123");
        assertThat(response.getWelfareBookStartDate()).isEqualTo(Timestamp.valueOf("2024-08-01 10:00:00"));
        assertThat(response.getWelfareBookEndDate()).isEqualTo(Timestamp.valueOf("2024-08-31 18:00:00"));
        assertThat(response.isWelfareBookIsCansle()).isFalse();
        assertThat(response.isWelfareBookIsComplete()).isFalse();
        assertThat(response.getWelfareBookNo()).isEqualTo(welfareBookNo);  // 자동 생성된 welfareBookNo 확인
        assertThat(response.getWelfareNo()).isEqualTo(1L);  // welfareNo 확인
    }

    @Test
    @DisplayName("복지 예약 삭제 테스트")
    public void testDeleteWelfareBook() {
        // Given
        CreateWelfareBookRequest request = CreateWelfareBookRequest.builder()
                .userId("user123")
                .welfareBookStartDate(Timestamp.valueOf("2024-08-01 10:00:00"))
                .welfareBookEndDate(Timestamp.valueOf("2024-08-31 18:00:00"))
                .welfareBookIsCansle(false)
                .welfareBookIsComplete(false)
                .welfareNo(1L)
                .build();
        Long welfareBookNo = welfareBookService.createWelfareBook(request);

        // When
        welfareBookService.deleteWelfareBook(welfareBookNo);  // welfareBookNo로 삭제

        // Then
        WelfareBookEntity deletedEntity = welfareBookRepository.findById(welfareBookNo).orElse(null);
        assertThat(deletedEntity).isNull();
    }
}
