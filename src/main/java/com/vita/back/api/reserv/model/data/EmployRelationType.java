package com.vita.back.api.reserv.model.data;

public enum EmployRelationType {
	SELF("00", "본인"),
    BROTHER("01", "형제"),
    SISTER("02", "자매"),
    SPOUSE("10", "배우자"),
    SPOUSE_BROTHER("23", "배우자 형제"),
    SPOUSE_SISTER("24", "배우자 자매"),
    CHILD("20", "자녀"),
    FATHER("30", "부"),
    SPOUSE_FATHER("31", "배우자부"),
    UNCLE("32", "백부"),
    MALE_UNCLE("33", "숙부"),
    FEMALE_UNCLE("34", "고모"),
    MOTHER("40", "모"),
    SPOUSE_MOTHER("41", "배우자모"),
    AUNT("44", "이모"),
    OTHER("50", "기타"),
    GRANDFATHER("70", "조부"),
    SPOUSE_GRANDFATHER("71", "배우자조부"),
    GRANDMOTHER("80", "조모"),
    SPOUSE_GRANDMOTHER("81", "배우자조모"),
    UNDEFINED("60", "미지정");

    private final String code;
    private final String description;

    EmployRelationType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    // 코드로 enum 찾기
    public static EmployRelationType fromCode(String code) {
        for (EmployRelationType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return UNDEFINED; // 코드에 해당하는 enum이 없는 경우 '미지정' 반환
    }
}
