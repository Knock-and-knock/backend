package com.shinhan.knockknock.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class TestEntity {

    @Id
    private int testNo;
}
