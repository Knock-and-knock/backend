package com.shinhan.knockknock.repository;

import com.shinhan.knockknock.domain.entity.WelfareBookEntity;
import com.shinhan.knockknock.domain.entity.WelfareEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WelfareBookRepository extends JpaRepository<WelfareBookEntity, Long> {
    List<WelfareBookEntity> findByUser_UserNo(Long userNo);
}
