package com.shinhan.knockknock.repository;

import com.shinhan.knockknock.domain.entity.WelfareBookEntity;
import com.shinhan.knockknock.domain.entity.WelfareEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WelfareBookRepository extends JpaRepository<WelfareBookEntity, Long> {
}
