package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.CreateUserRequest;
import com.shinhan.knockknock.domain.entity.UserEntity;
import com.shinhan.knockknock.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.model.MessageType;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private DefaultMessageService defualtMessageService;

    @Value("${coolsms.api.key}")
    private String apiKey;

    @Value("${coolsms.api.secret}")
    private String apiSecret;

    @Value("${coolsms.api.number}")
    private String fromPhoneNumber;

    @PostConstruct
    private void initializeMessageService() {
        this.defualtMessageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

    @Override
    public Boolean readUserId(String userId) {
        UserEntity findUser = userRepository.findByUserId(userId).orElse(null);
        return findUser == null;
    }

    @Override
    public Boolean readUserPhone(String phone) {
        UserEntity findUser = userRepository.findByUserPhone(phone);
        return findUser == null;
    }

    @Override
    public SingleMessageSentResponse sendSms(String phone, String validationNum) {
        SingleMessageSentResponse messageSentResponse = sendMessage(phone, validationNum);
        System.out.println(messageSentResponse);
        return messageSentResponse;
    }

    @Override
    public Boolean createUser(CreateUserRequest request) {
        UserEntity entity = dtoToEntity(request);
        entity.setUserPassword(passwordEncoder.encode(request.getUserPassword()));
        if(request.getUserSimplePassword() != null) {
            entity.setUserSimplePassword(passwordEncoder.encode(request.getUserSimplePassword()));
        }
        if(userRepository.existsByUserId(entity.getUserId())) {
            throw new DuplicateKeyException("이미 존재하는 아이디입니다.");
        }
        try {
            UserEntity createUser = userRepository.save(entity);
            if(createUser.getUserNo() != null) return true;
        } catch(DataIntegrityViolationException exception) {
            if (exception.getCause() instanceof ConstraintViolationException) {
                // UNIQUE 제약조건 위반 시 처리할 코드
                throw new DuplicateKeyException("이미 가입된 회원입니다.");
            } else {
                // 기타 데이터 무결성 위반 처리
                throw new DataIntegrityViolationException("잘못된 요청입니다.");
            }
        }
        return false;
    }

    private SingleMessageSentResponse sendMessage(String phone, String validationNum) {
        Message message = new Message();
        message.setFrom(fromPhoneNumber);
        message.setTo(phone);
        message.setType(MessageType.SMS);
        message.setText("[똑똑] 인증번호는 [" + validationNum + "] 입니다.");

        return this.defualtMessageService.sendOne(new SingleMessageSendingRequest(message));
    }
}
