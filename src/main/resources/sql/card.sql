INSERT INTO card_tb (
    card_no,
    card_cvc,
    card_ename,
    card_password,
    card_bank,
    card_account,
    card_amountdate,
    card_expiredate,
    cardissue_no,
    user_no,
    card_isfamily,
    card_address,
    card_userkname,
    card_userphone
) VALUES
      ('1234567890123456', '123', 'John Doe', 'password123', 'Shinhan Bank', '110-123-456789', '12', '2025-12-31', 1001, 1, false, '123 Main St, Seoul, Korea','보호자01','01012345678'),
      ('6543210987654321', '456', 'Jane Smith', 'securePass456', 'Kookmin Bank', '220-987-654321', '15', '2026-06-30', 1002, 2, true, '456 Elm St, Busan, Korea','피보호자01','01012345678'),
      ('1111222233334444', '789', 'Alice Johnson', 'pass789!', 'Hana Bank', '330-123-987654', '18', '2027-03-31', 1003, 3, false, '789 Oak St, Incheon, Korea','보호자03','01099998888'),
      ('4444333322221111', '101', 'Bob Lee', 'qwerty101', 'Woori Bank', '440-456-123456', '25', '2028-09-30', 1004, 4, true, '321 Pine St, Daegu, Korea','보호자02','01012345677'),
      ('9999888877776666', '202', 'Charlie Brown', 'charlie202!', 'NH Nonghyup Bank', '550-789-321654', '05', '2024-05-31', 1005, 5, false, '654 Cedar St, Ulsan, Korea','피보호자02','01012345677');
