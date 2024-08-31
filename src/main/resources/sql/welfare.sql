INSERT INTO Welfare_tb (welfare_no, welfare_name, welfare_price, welfare_category) VALUES
(1, '일상가사', 10000, '돌봄'),
(2, '가정간병', 15000, '돌봄'),
(3, '한울', 20000, '돌봄')
ON CONFLICT (welfare_no) DO NOTHING;
