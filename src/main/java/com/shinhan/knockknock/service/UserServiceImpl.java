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
        UserEntity findUser = userRepository.findByUserId(userId);
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
        try {
            UserEntity createUser = userRepository.save(entity);
            if(createUser.getUserNo() != null) return true;
        } catch(DataIntegrityViolationException exception) {
            if (exception.getCause() instanceof ConstraintViolationException) {
                // UNIQUE 제약조건 위반 시 처리할 코드
                System.out.println("유니크 제약조건 위반 발생!");
                // 예를 들어 사용자에게 중복된 값이 있다는 메시지를 반환하거나 다른 처리를 수행할 수 있습니다.
            } else {
                // 기타 데이터 무결성 위반 처리
                System.out.println("기타 데이터 무결성 위반 발생!");
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