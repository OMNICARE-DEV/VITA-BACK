package com.vita.back.api.model;

import lombok.Getter;

public class Constant {
    public final static boolean SUCCESS = true;
    public final static boolean FAIL = false;
    
    public final static String YES = "Y";
    public final static String NO = "N";
    
    public static class Admin {
        public final static String U2BIO = "SYS_U2BIO";
    }
    
    public static class ChangeDivCd {
        public final static String STATUS = "01";
        public final static String DATETIME = "02";
        public final static String PRODUCT = "03";
        public final static String ETC = "04";
    }
    
    public static class ReservSt {
        public final static String CHECKUP_COMPLETE = "70";
        public final static String RESERV_CANCEL = "80";
    }

    @Getter
    public enum DataFormatEnum {
        STRING("문자", "^.*$", "", ""),
        NUMBER("숫자", "^[0-9]*$", "[^0-9]", "숫자만 입력 가능합니다"),
        ENGLISH_LOWER("영문 소문자", "^[a-z]*$", "[^a-z]", "영문 소문자만 입력 가능합니다."),
        ENGLISH_UPPER("영문 대문자", "^[A-Z]*$", "[^A-Z]", "영문 대문자만 입력 가능합니다."),
        ENGLISH_NUMBER("영문 숫자", "^[a-zA-Z0-9]*$", "[^a-zA-Z0-9]", "영문/숫자만 입력 가능합니다."),
        ENGLISH("영문", "^[a-zA-Z]*$", "[^a-zA-Z]", "영문만 입력 가능합니다."),
        KOREAN("한글", "^[가-힣]*$", "[^가-힣]", "한글만 입력 가능합니다."),
        ENGLISH_KOREAN("영문/한글", "^[a-zA-Z가-힣]*$", "[^a-zA-Z가-힣]", "영문/한글만 입력가능합니다."),
        EXCLUDING_SPECIAL_CHAR("특수문자제외", "^[a-zA-Z가-힣0-9]*$", "[^a-zA-Z가-힣0-9]", "특수문자 입력이 불가능합니다."),
        EMAIL("이메일", "^([a-zA-Z0-9\\.\\-\\_]+@[a-zA-Z0-9]+\\.[a-z]{1,3}(\\.[a-z]{2,3})?)?$", "[^a-zA-Z0-9@\\.\\-\\_]", "이메일 형식을 확인해주세요."),
        PHONE("전화번호", "^([0-9]{7,13})?$", "[^0-9]", "전화번호 형식을 확인해주세요."),
        DATE_8("8자리 날짜", "^(([1-2]{1}[0-9]{3})(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1]))?$", "[^0-9]", "8자리 날짜 입력 형식을 확인해주세요."),
        DATE_6("6자리 날짜", "^(([0-9]{2})(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1]))?$", "[^0-9]", "6자리 날짜 입력 형식을 확인해주세요."),
        TIME_4("4자리 시간", "^((0[0-9]|1[0-9]|2[0-3])([0-5][0-9]))?$", "[^0-9]", "4자리 시간 입력 형식을 확인해주세요."),
        BIRTHDAY_8("8자리 생년월일", "^((19[0-9]{2}|20[0-9]{2})(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1]))?$", "[^0-9]", "8자리 생년월일 입력 형식을 확인해주세요."),
        BIRTHDAY_6("6자리 생년월일", "^(([0-9]{2})(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1]))?$", "[^0-9]", "6자리 생년월일 입력 형식을 확인해주세요."),
        CORP_NO("사업자번호", "^([0-9]{10})?$", "[^0-9]", "사업자번호 입력 형식을 확인해주세요(숫자 10자리)."),
        CUSTOMER_ID("고객사ID", "^([c|C]{1}[0-9]{9})?$", "[^cC0-9]", "고객사ID 입력 형식을 확인해주세요(c000000001/C000000001)."),
        YEAR("년도", "^((1|2)[0-9]{3})?$", "[^0-9]", "4자리 연도 입력 형식을 확인해주세요."),
        GENDER("성별", "^((남(성|자)?)|(여(성|자)?|10|20))?$", "[^120남여자성]", "성별 입력 형식을 확인해주세요(10:남/남자/남성|20:여/여자/여성)"),
        RELATION_TYPE(
            "관계구분", 
            "^(본인|형제|자매|배우자|자녀|부|배우자부|백부|숙부|고모|모|배우자모|이모|기타|조부|배우자조무|조모|배우자조모|00|01|02|10|20|30|31|32|33|34|40|41|44|70|71|80|81|50)?$", "[^12345780가-힣]", 
            "관계 입력 형식을 확인해주세요(00:본인/01:형제/02:자매/10:배우자/20:자녀/30:부/31:배우자부/32:백부/33:숙부/34:고모/40:모/41:배우자모/44:이모/70:조부/71:배우자조부/80:조모/81:배우자조모/50:기타)."
        ),
        DOMESTIC_YN("내국인여부", "^(Y|y|N|n)?$", "^[^a-zA-Z]?$", "내국인여부 입력 형식을 확인해주세요(Y/N/y/n)."),
        YN("여부", "^(Y|y|N|n)?$", "^[^a-zA-Z]?$", "여부 입력 형식을 확인해주세요(Y/N/y/n)."),
        CHECKUP_DIV_CD("검진구분", "^(종합검진|일반검진|특수검진|채용검진|10|20|30|40)?$", "[^12340가-힣]", "검진구분 입력 형식을 확인해주세요(10:종합검진/20:일반검진/30:특수검진/40:채용검진)."),
        COMPANY_SUPPORT_TYPE("회사지원구분", "^(없음|회사지급|본인선지급|10|20|00)?$", "[^120가-힣]", "회사지원구분 입력 형식을 확인해주세요(00:없음/10:회사지급/20:본인선지급)."),
        PARTNER_CENTER_KMI("KMI병원코드", "^([h|H]{1}0000[1-8]{1})?$", "[^hH0-9]", "KMI병원코드 입력 형식을 확인해주세요(H00001~H00008/h00001~h00008)."),
        PARTNER_CENTER("병원코드", "^([h|H]{1}[0-9]{5})?$", "[^hH0-9]", "병원코드 입력 형식을 확인해주세요(H00000)."),
        JOB_TYPE("직종구분", "^(생산직|사무직|10|20)?$", "[^120가-힣]", "직종구분 입력 형식을 확인해주세요(10:생산직/20:사무직)."),
        AM_PM("오전/오후", "^(am|pm|AM|PM|오전|오후)?$", "[^ampmAMPM오전후]", "오전/오후 입력 형식을 확인해주세요(am/AM/오전/pm/PM/오후)."),
        ITEM_LIST("검사리스트", "^([a-zA-Z0-9]{6},?)*$", "[^a-zA-Z0-9,]", "검사리스트 입력형식을 확인해주세요(6자리코드/구분자(,))."),    
        RESULT_RECIEVE_WAY("결과수신방법", "^(이메일|우편|방문수령|전자등기|10|20|30|40)?$", "[^가-힣0-4]", "결과수신방법 입력 형식을 확인해주세요(10:이메일/20:우편/30:방문수령/40:전자등기).");
        
        private final String name;
        private final String validRegex;
        private final String replaceRegex;
        private final String message;
        
        DataFormatEnum(String name, String validRegex, String replaceRegex, String message) {
            this.name = name;
            this.validRegex = validRegex;
            this.replaceRegex = replaceRegex;
            this.message = message;
        }
    }
}
