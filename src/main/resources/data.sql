
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

-- welfare_tb
INSERT INTO Welfare_tb (welfare_no, welfare_name, welfare_price, welfare_category) VALUES
(1, '일상가사', 10000, '돌봄'),
(2, '가정간병', 15000, '돌봄'),
(3, '한울', 20000, '돌봄')
ON CONFLICT (welfare_no) DO NOTHING;

-- Welfarebook_tb
-- welfarebook_no가 1인 데이터가 없을 때만 삽입
INSERT INTO Welfarebook_tb (welfarebook_no, welfarebook_startdate, welfarebook_enddate, welfarebook_iscansle, welfarebook_iscomplete, welfarebook_usetime, welfarebook_totalprice, user_no, welfare_no, welfarebook_reservationdate)
SELECT 1, '2024-07-26', '2024-07-26', false, true, NULL, 60000, 2, 1, '2024-07-26 10:00:00'
WHERE NOT EXISTS (SELECT 1 FROM Welfarebook_tb WHERE welfarebook_no = 1);

-- welfarebook_no가 2인 데이터가 없을 때만 삽입
INSERT INTO Welfarebook_tb (welfarebook_no, welfarebook_startdate, welfarebook_enddate, welfarebook_iscansle, welfarebook_iscomplete, welfarebook_usetime, welfarebook_totalprice, user_no, welfare_no, welfarebook_reservationdate)
SELECT 2, '2024-07-28', '2024-07-28', false, true, NULL, 60000, 2, 1, '2024-07-27 10:00:00'
WHERE NOT EXISTS (SELECT 1 FROM Welfarebook_tb WHERE welfarebook_no = 2);

-- welfarebook_no가 3인 데이터가 없을 때만 삽입
INSERT INTO Welfarebook_tb (welfarebook_no, welfarebook_startdate, welfarebook_enddate, welfarebook_iscansle, welfarebook_iscomplete, welfarebook_usetime, welfarebook_totalprice, user_no, welfare_no, welfarebook_reservationdate)
SELECT 3, '2024-08-02', '2024-08-02', true, false, NULL, 0, 2, 1, '2024-08-01 14:00:00'
WHERE NOT EXISTS (SELECT 1 FROM Welfarebook_tb WHERE welfarebook_no = 3);

-- welfarebook_no가 4인 데이터가 없을 때만 삽입
INSERT INTO Welfarebook_tb (welfarebook_no, welfarebook_startdate, welfarebook_enddate, welfarebook_iscansle, welfarebook_iscomplete, welfarebook_usetime, welfarebook_totalprice, user_no, welfare_no, welfarebook_reservationdate)
SELECT 4, '2024-08-16', '2024-08-16', false, true, NULL, 70000, 2, 1, '2024-08-15 16:00:00'
WHERE NOT EXISTS (SELECT 1 FROM Welfarebook_tb WHERE welfarebook_no = 4);

-- welfarebook_no가 4인 데이터가 없을 때만 삽입
INSERT INTO Welfarebook_tb (welfarebook_no, welfarebook_startdate, welfarebook_enddate, welfarebook_iscansle, welfarebook_iscomplete, welfarebook_usetime, welfarebook_totalprice, user_no, welfare_no, welfarebook_reservationdate)
SELECT 5, '2024-08-28', '2024-08-28', false, false, NULL, 170000, 2, 1, '2024-08-26 16:00:00'
WHERE NOT EXISTS (SELECT 1 FROM Welfarebook_tb WHERE welfarebook_no = 5);
