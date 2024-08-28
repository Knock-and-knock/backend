-- card
DELETE FROM cardhistory_tb;
DELETE FROM cardcategory_tb;
INSERT INTO cardcategory_tb (cardcategory_no, cardcategory_name) VALUES (1,'식비');
INSERT INTO cardcategory_tb (cardcategory_no, cardcategory_name) VALUES (2,'잡화');
INSERT INTO cardcategory_tb (cardcategory_no, cardcategory_name) VALUES (3,'교통');
INSERT INTO cardcategory_tb (cardcategory_no, cardcategory_name) VALUES (4,'생활');
INSERT INTO cardcategory_tb (cardcategory_no, cardcategory_name) VALUES (5,'쇼핑');
INSERT INTO cardcategory_tb (cardcategory_no, cardcategory_name) VALUES (6,'유흥');
INSERT INTO cardcategory_tb (cardcategory_no, cardcategory_name) VALUES (7,'의료');
INSERT INTO cardcategory_tb (cardcategory_no, cardcategory_name) VALUES (8,'기타');
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '스타벅스 홍대점', 40000, '2024-01-15 10:20:00.000', 1);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '이마트 강남점', 95000, '2024-01-20 11:30:00.000', 5);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '롯데시네마 강남점', 15000, '2024-01-25 14:00:00.000', 6);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '맥도날드 서울역점', 18000, '2024-01-05 13:15:00.000', 1);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, 'CU 홍대점', 8000, '2024-01-14 08:45:00.000', 2);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '메가박스 신촌점', 13000, '2024-01-19 19:50:00.000', 6);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '배스킨라빈스 강남점', 25000, '2024-01-10 16:30:00.000', 1);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '이마트 신촌점', 120000, '2024-01-10 12:20:00.000', 5);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, 'CGV 홍대점', 14000, '2024-01-15 18:00:00.000', 6);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '빕스 여의도점', 60000, '2024-01-10 12:45:00.000', 1);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '다이소 강남역점', 15000, '2024-02-15 17:10:00.000', 2);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '이케아 고양점', 95000, '2024-02-20 11:00:00.000', 5);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '커피빈 강남역점', 35000, '2024-02-05 09:30:00.000', 1);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, 'CU 신사점', 9000, '2024-02-10 21:45:00.000', 2);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '현대백화점 강남점', 420000, '2024-02-15 15:30:00.000', 5);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '버거킹 홍대점', 22000, '2024-02-02 13:00:00.000', 1);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '뉴발란스 명동점', 110000, '2024-02-07 16:40:00.000', 5);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, 'CGV 용산점', 14000, '2024-02-12 20:00:00.000', 6);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '이마트 영등포점', 98000, '2024-02-18 14:25:00.000', 5);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '코엑스 스타벅스', 42000, '2024-02-22 15:45:00.000', 1);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '이케아 광명점', 115000, '2024-03-03 11:00:00.000', 5);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '빕스 압구정점', 68000, '2024-03-05 13:10:00.000', 1);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '롯데마트 송파점', 102000, '2024-03-07 16:30:00.000', 5);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '다이소 마포점', 12000, '2024-03-10 09:45:00.000', 2);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '맥도날드 신촌점', 15000, '2024-03-12 14:20:00.000', 1);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, 'CGV 압구정점', 15000, '2024-03-14 19:00:00.000', 6);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '나이키 홍대점', 99000, '2024-03-17 18:10:00.000', 5);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '배스킨라빈스 신촌점', 23000, '2024-03-19 20:50:00.000', 1);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '세븐일레븐 강남점', 8000, '2024-03-22 08:40:00.000', 2);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '올리브영 강남점', 50000, '2024-03-25 15:30:00.000', 4);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '다이소 여의도점', 18000, '2024-04-01 11:10:00.000', 2);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '스타벅스 여의도점', 35000, '2024-04-02 09:15:00.000', 1);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '신세계백화점 강남점', 330000, '2024-04-05 16:30:00.000', 5);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '교보문고 강남점', 28000, '2024-04-07 14:10:00.000', 4);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '롯데시네마 홍대점', 14000, '2024-04-09 20:30:00.000', 6);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '맥도날드 강남역점', 18000, '2024-04-12 12:50:00.000', 1);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '이마트 서초점', 85000, '2024-04-15 18:40:00.000', 5);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, 'GS25 신촌점', 7000, '2024-04-17 08:30:00.000', 2);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, 'CGV 강변점', 13500, '2024-04-19 19:10:00.000', 6);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '코엑스 몰', 45000, '2024-04-22 17:50:00.000', 4);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '롯데마트 잠실점', 92000, '2024-05-01 14:40:00.000', 5);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '배스킨라빈스 홍대점', 23000, '2024-05-03 20:10:00.000', 1);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '이케아 고양점', 125000, '2024-05-05 11:30:00.000', 5);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '스타벅스 강남역점', 34000, '2024-05-07 10:20:00.000', 1);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '다이소 신촌점', 17000, '2024-05-10 12:00:00.000', 2);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '롯데시네마 영등포점', 15000, '2024-05-12 15:30:00.000', 6);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, 'GS25 강남점', 8000, '2024-05-15 07:50:00.000', 2);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '신세계백화점 영등포점', 300000, '2024-05-17 16:40:00.000', 5);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '올리브영 신촌점', 38000, '2024-05-20 18:10:00.000', 4);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, 'CGV 용산점', 14500, '2024-05-23 21:00:00.000', 6);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '이마트 송파점', 105000, '2024-06-01 13:20:00.000', 5);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '세븐일레븐 홍대점', 9000, '2024-06-03 08:30:00.000', 2);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '스타벅스 여의도점', 37000, '2024-06-05 10:10:00.000', 1);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '롯데마트 서초점', 89000, '2024-06-07 16:50:00.000', 5);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '맥도날드 강남점', 20000, '2024-06-09 12:30:00.000', 1);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, 'CGV 압구정점', 14500, '2024-06-11 19:40:00.000', 6);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '코엑스 올리브영', 48000, '2024-06-13 15:20:00.000', 4);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '나이키 홍대점', 115000, '2024-06-15 17:30:00.000', 5);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '이케아 광명점', 122000, '2024-06-17 14:10:00.000', 5);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '올리브영 강남역점', 51000, '2024-06-19 18:00:00.000', 4);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '올리브영 연남점', 30000, '2024-07-23 09:53:16.670', 4);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '홍콩반점 연남점', 20000, '2024-07-23 09:53:16.670', 1);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '역전할맥 연남점', 30000, '2024-07-23 09:53:16.670', 6);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '스타벅스 강남점', 45000, '2024-07-23 09:53:16.670', 1);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, 'CU 신촌점', 5000, '2024-07-23 09:53:16.670', 2);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '이마트 용산점', 120000, '2024-07-23 09:53:16.670', 5);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '롯데시네마 영등포점', 15000, '2024-07-23 09:53:16.670', 6);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '현대백화점 압구정점', 300000, '2024-07-23 09:53:16.670', 5);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '김밥천국 신촌점', 8000, '2024-07-23 09:53:16.670', 1);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '세브란스 병원', 100000, '2024-07-23 09:53:16.670', 7);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '교보문고 광화문점', 25000, '2024-07-23 09:53:16.670', 4);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '코엑스 메가박스', 12000, '2024-08-23 09:53:16.670', 6);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '다이소 강남점', 15000, '2024-08-23 09:53:16.670', 2);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '이케아 광명점', 80000, '2024-08-23 09:53:16.670', 5);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '스타벅스 신촌점', 40000, '2024-08-23 09:53:16.670', 1);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '아웃백 강남점', 70000, '2024-08-23 09:53:16.670', 1);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '빕스 신촌점', 60000, '2024-08-23 09:53:16.670', 1);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, 'CGV 강남점', 13000, '2024-08-23 09:53:16.670', 6);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '홍콩반점 종각점', 18000, '2024-08-23 09:53:16.670', 1);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '이디야 커피 강남점', 30000, '2024-08-23 09:53:16.670', 1);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '가성비마트 영등포점', 25000, '2024-08-23 09:53:16.670', 2);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '나이키 강남점', 100000, '2024-08-23 09:53:16.670', 5);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '뉴발란스 명동점', 120000, '2024-08-23 09:53:16.670', 5);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, 'CU 강남점', 7000, '2024-08-23 09:53:16.670', 2);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '롯데마트 용산점', 75000, '2024-08-23 09:53:16.670', 4);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '이케아 광명점', 100000, '2024-08-23 09:53:16.670', 5);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '현대백화점 신촌점', 450000, '2024-08-23 09:53:16.670', 5);
INSERT INTO cardhistory_tb (card_id, cardhistory_shopname, cardhistory_amount, cardhistory_approve, cardcategory_no) values (1, '가성비마트 신촌점', 15000, '2024-08-23 09:53:16.670', 2);

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
