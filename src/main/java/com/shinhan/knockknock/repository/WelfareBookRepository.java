package com.shinhan.knockknock.repository;

import com.shinhan.knockknock.domain.entity.WelfareBookEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface WelfareBookRepository extends JpaRepository<WelfareBookEntity, Long> {
    List<WelfareBookEntity> findByUser_UserNo(Long userNo, Sort sort);
    List<WelfareBookEntity> findByUser_UserNoAndWelfareBookReservationDateBetween(Long userNo, Timestamp startDate, Timestamp endDate);
}
