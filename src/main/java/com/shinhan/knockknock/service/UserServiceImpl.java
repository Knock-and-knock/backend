package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.CreateUserRequest;
import com.shinhan.knockknock.domain.dto.ReadUserResponse;
import com.shinhan.knockknock.domain.entity.UserEntity;
import com.shinhan.knockknock.domain.entity.UserRoleEnum;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Override
    public ReadUserResponse readUser() {
        // 로그인된 사용자 인증 정보로 UserEntity 객체 가져오기
        String protegeId = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUserId(protegeId)
                .orElseThrow(() -> new UsernameNotFoundException("회원이 존재하지 않습니다."));
        ReadUserResponse readUserResponse = null;
        if(user.getUserType().equals(UserRoleEnum.PROTECTOR)){  // 보호자인 경우
            if(user.getMatchProtege().getUserProtege() != null) { // 매칭 정보가 있는 경우
                UserEntity protege = user.getMatchProtege().getUserProtege();
                readUserResponse = ReadUserResponse.builder()
                        .userNo(user.getUserNo())
                        .userId(user.getUserId())
                        .userName(user.getUserName())
                        .userType(user.getUserType())
                        .userPhone(user.getUserPhone())
                        .protegeName(protege.getUserName())
                        .protegeBirth(protege.getUserBirth())
                        .protegeGender(protege.getUserGender())
                        .protegeHeight(protege.getUserHeight())
                        .protegeWeight(protege.getUserWeight())
                        .protegeDisease(protege.getUserDisease())
                        .protegeAddress(protege.getUserAddress())
                        .build();
            } else {
                readUserResponse = ReadUserResponse.builder()
                        .userNo(user.getUserNo())
                        .userId(user.getUserId())
                        .userName(user.getUserName())
                        .userPhone(user.getUserPhone())
                        .userType(user.getUserType())
                        .build();
            }
        } else if(user.getUserType().equals(UserRoleEnum.PROTEGE)){ // 피보호자인 경우
            readUserResponse = ReadUserResponse.builder()
                    .userNo(user.getUserNo())
                    .userId(user.getUserId())
                    .userName(user.getUserName())
                    .userType(user.getUserType())
                    .userPhone(user.getUserPhone())
                    .protegeName(user.getUserName())
                    .protegeBirth(user.getUserBirth())
                    .protegeGender(user.getUserGender())
                    .protegeHeight(user.getUserHeight())
                    .protegeWeight(user.getUserWeight())
                    .protegeDisease(user.getUserDisease())
                    .protegeAddress(user.getUserAddress())
                    .build();
        } else {
            throw new RuntimeException("잘못된 요청입니다.");
        }

        return readUserResponse;
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
