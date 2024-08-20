package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.welfarebook.CreateWelfareBookRequest;
import com.shinhan.knockknock.domain.dto.welfarebook.ReadWelfareBookResponse;
import com.shinhan.knockknock.domain.entity.WelfareBookEntity;
import com.shinhan.knockknock.domain.entity.UserEntity;
import com.shinhan.knockknock.domain.entity.WelfareEntity;
import com.shinhan.knockknock.repository.UserRepository;
import com.shinhan.knockknock.repository.WelfareBookRepository;
import com.shinhan.knockknock.repository.WelfareRepository;
import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WelfareRepository welfareRepository;

    private UserEntity user;
    private WelfareEntity welfare;

    @BeforeEach
    void setUp() {
        user = userRepository.save(UserEntity.builder().userNo(1L).userName("user123").build());
        welfare = welfareRepository.save(WelfareEntity.builder().welfareNo(1L).welfareName("가정 돌봄").welfarePrice(6000L).build());
    }

    @Test
    @DisplayName("복지 예약 생성 테스트")
    public void testCreateWelfareBook() {
        // Given
        CreateWelfareBookRequest request = CreateWelfareBookRequest.builder()
                .userNo(user.getUserNo())
                .welfareBookStartDate(Timestamp.valueOf("2024-08-01 10:00:00"))
                .welfareBookEndDate(Timestamp.valueOf("2024-08-31 18:00:00"))
                .welfareBookIsCansle(false)
                .welfareBookIsComplete(false)
                .welfareName(welfare.getWelfareName())
                .welfarePirce(welfare.getWelfarePrice())
                .build();

        // When
        Long welfareBookNo = welfareBookService.createWelfareBook(request);

        // Then
        WelfareBookEntity savedEntity = welfareBookRepository.findById(welfareBookNo).orElse(null);
        assertThat(savedEntity).isNotNull();
        assertThat(savedEntity.getUserNo()).isEqualTo(user.getUserNo());
        assertThat(savedEntity.getWelfareBookStartDate()).isEqualTo(Timestamp.valueOf("2024-08-01 10:00:00"));
        assertThat(savedEntity.getWelfareBookEndDate()).isEqualTo(Timestamp.valueOf("2024-08-31 18:00:00"));
        assertThat(savedEntity.isWelfareBookIsCansle()).isFalse();
        assertThat(savedEntity.isWelfareBookIsComplete()).isFalse();
        assertThat(savedEntity.getWelfare().getWelfareName()).isEqualTo(welfare.getWelfareName());
        assertThat(savedEntity.getWelfare().getWelfarePrice()).isEqualTo(welfare.getWelfarePrice());
    }

    @Test
    @DisplayName("복지 예약 사용자별 전체 조회 테스트")
    public void testReadAllWelfareBooksByUserNo() {
        // Given
        CreateWelfareBookRequest request1 = CreateWelfareBookRequest.builder()
                .userNo(user.getUserNo())
                .welfareBookStartDate(Timestamp.valueOf("2024-08-01 10:00:00"))
                .welfareBookEndDate(Timestamp.valueOf("2024-08-31 18:00:00"))
                .welfareBookIsCansle(false)
                .welfareBookIsComplete(false)
                .welfareName(welfare.getWelfareName())
                .welfarePirce(welfare.getWelfarePrice())
                .build();

        CreateWelfareBookRequest request2 = CreateWelfareBookRequest.builder()
                .userNo(user.getUserNo())
                .welfareBookStartDate(Timestamp.valueOf("2024-09-01 10:00:00"))
                .welfareBookEndDate(Timestamp.valueOf("2024-09-30 18:00:00"))
                .welfareBookIsCansle(false)
                .welfareBookIsComplete(false)
                .welfareName(welfare.getWelfareName())
                .welfarePirce(welfare.getWelfarePrice())
                .build();

        welfareBookService.createWelfareBook(request1);
        welfareBookService.createWelfareBook(request2);

        // When
        List<ReadWelfareBookResponse> welfareBookList = welfareBookService.readAllByUserNo(user.getUserNo());

        // Then
        assertThat(welfareBookList).hasSize(2);
    }

    @Test
    @DisplayName("복지 예약 상세 조회 테스트")
    public void testReadDetailWelfareBook() {
        // Given
        CreateWelfareBookRequest request = CreateWelfareBookRequest.builder()
                .userNo(user.getUserNo())
                .welfareBookStartDate(Timestamp.valueOf("2024-08-01 10:00:00"))
                .welfareBookEndDate(Timestamp.valueOf("2024-08-31 18:00:00"))
                .welfareBookIsCansle(false)
                .welfareBookIsComplete(false)
                .welfareName(welfare.getWelfareName())
                .welfarePirce(welfare.getWelfarePrice())
                .build();
        Long welfareBookNo = welfareBookService.createWelfareBook(request);

        // When
        ReadWelfareBookResponse response = welfareBookService.readDetail(welfareBookNo);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getWelfareBookStartDate()).isEqualTo(Timestamp.valueOf("2024-08-01 10:00:00"));
        assertThat(response.getWelfareBookEndDate()).isEqualTo(Timestamp.valueOf("2024-08-31 18:00:00"));
        assertThat(response.isWelfareBookIsCansle()).isFalse();
        assertThat(response.isWelfareBookIsComplete()).isFalse();
        assertThat(response.getWelfareName()).isEqualTo(welfare.getWelfareName());
        assertThat(response.getWelfarePirce()).isEqualTo(welfare.getWelfarePrice());
    }

    @Test
    @DisplayName("복지 예약 삭제 테스트")
    public void testDeleteWelfareBook() {
        // Given
        CreateWelfareBookRequest request = CreateWelfareBookRequest.builder()
                .userNo(user.getUserNo())
                .welfareBookStartDate(Timestamp.valueOf("2024-08-01 10:00:00"))
                .welfareBookEndDate(Timestamp.valueOf("2024-08-31 18:00:00"))
                .welfareBookIsCansle(false)
                .welfareBookIsComplete(false)
                .welfareName(welfare.getWelfareName())
                .welfarePirce(welfare.getWelfarePrice())
                .build();
        Long welfareBookNo = welfareBookService.createWelfareBook(request);

        // When
        welfareBookService.deleteWelfareBook(welfareBookNo);

        // Then
        WelfareBookEntity deletedEntity = welfareBookRepository.findById(welfareBookNo).orElse(null);
        assertThat(deletedEntity).isNull();
    }
}
