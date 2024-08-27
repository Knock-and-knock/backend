/*
  Category
 */
INSERT INTO cardcategory_tb (cardcategory_name) VALUES ('식비');
INSERT INTO cardcategory_tb (cardcategory_name) VALUES ('잡화');
INSERT INTO cardcategory_tb (cardcategory_name) VALUES ('교통');
INSERT INTO cardcategory_tb (cardcategory_name) VALUES ('생활');
INSERT INTO cardcategory_tb (cardcategory_name) VALUES ('쇼핑');
INSERT INTO cardcategory_tb (cardcategory_name) VALUES ('유흥');
INSERT INTO cardcategory_tb (cardcategory_name) VALUES ('의료');
INSERT INTO cardcategory_tb (cardcategory_name) VALUES ('기타');

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
