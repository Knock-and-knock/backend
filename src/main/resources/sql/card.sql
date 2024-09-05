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
      ('1234-5678-9012-3456', '123', 'John Doe', 'password123', 'Shinhan Bank', '110-123-456789', '12', '2025-12-31', 1001, 1, false, '123 Main St, Seoul, Korea','보호자01','01011111111'),
      ('6543-2109-8765-4321', '456', 'Jane Smith', 'securePass456', 'Kookmin Bank', '220-987-654321', '15', '2026-06-30', 1002, 2, true, '456 Elm St, Busan, Korea','피보호자01','01022222222'),
      ('1111-2222-3333-4444', '789', 'Alice Johnson', 'pass789!', 'Hana Bank', '330-123-987654', '18', '2027-03-31', 1003, 3, false, '789 Oak St, Incheon, Korea','보호자03','01033333333'),
      ('4444-3333-2222-1111', '101', 'Bob Lee', 'qwerty101', 'Woori Bank', '440-456-123456', '25', '2028-09-30', 1004, 4, true, '321 Pine St, Daegu, Korea','보호자02','01044444444'),
      ('9999-8888-7777-6666', '202', 'Charlie Brown', 'charlie202!', 'NH Nonghyup Bank', '550-789-321654', '05', '2024-05-31', 1005, 5, false, '654 Cedar St, Ulsan, Korea','피보호자02','01055555555'),
      ('1234-5678-1234-5678', '222', 'bohozanim', '1234', 'NH Nonghyup Bank', '551-789-321654', '05', '2024-05-31', 1005, 9, false, '654 Cedar St, Ulsan, Korea','보호자님','01066666666'),
      ('8765-4321-8765-4321', '333', 'pibohozanim', '1234', 'NH Nonghyup Bank', '552-789-321654', '05', '2024-05-31', 1005, 10, false, '654 Cedar St, Ulsan, Korea','피보호자님','01077777777');
