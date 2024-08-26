
/*
 cardissue_tb data
 */

INSERT INTO cardissue_tb (
    cardissue_address,
    cardissue_ename,
    cardissue_email,
    cardissue_bank,
    cardissue_account,
    cardissue_isagree,
    cardissue_income,
    cardissue_credit,
    cardissue_amountdate,
    cardissue_source,
    cardissue_ishighrisk,
    cardissue_purpose,
    user_no,
    cardissue_isfamily
) VALUES (
             '서버 시작 테스트',  -- 주소
             'YangSeungGeon',         -- 영문 이름
             '@.@.@.com',  -- 이메일
             '카카오뱅크',        -- 은행 이름
             '12345678901234',   -- 계좌 번호
             true,               -- 동의 여부
             50000,              -- 연간소득
             750,                -- 신용 점수
             '2024-08-10',       -- 결제 일자
             'working', -- 자금의 원천
             'HIGHRISK',              -- 고위험 여부
             'Personal Use',     -- 거래 목적
             1,                   -- 유저 번호
            false
         );