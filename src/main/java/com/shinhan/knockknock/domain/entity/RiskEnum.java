package com.shinhan.knockknock.domain.entity;

public enum RiskEnum {
    highRisk, mediumRisk, noRisk, UNKNOWN;

    public static RiskEnum fromString(String value) {
        if (value == null) {
            return UNKNOWN;
        }

        try {
            return RiskEnum.valueOf(value);
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}
