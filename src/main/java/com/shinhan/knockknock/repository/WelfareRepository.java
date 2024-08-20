package com.shinhan.knockknock.repository;

import com.shinhan.knockknock.domain.entity.WelfareEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WelfareRepository extends JpaRepository<WelfareEntity, Long> {
    Optional<WelfareEntity> findByWelfareNameAndWelfarePrice(String welfareName, Long welfarePirce);
}
