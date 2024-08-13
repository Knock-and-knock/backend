package com.shinhan.knockknock.service;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

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

    private SingleMessageSentResponse sendMessage(String phone, String validationNum) {
        Message message = new Message();
        message.setFrom(fromPhoneNumber);
        message.setTo(phone);
        message.setType(MessageType.SMS);
        message.setText("[똑똑] 인증번호는 [" + validationNum + "] 입니다.");

        return this.defualtMessageService.sendOne(new SingleMessageSendingRequest(message));
    }
}
