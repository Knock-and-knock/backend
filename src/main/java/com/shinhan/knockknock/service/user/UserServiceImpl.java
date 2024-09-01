package com.shinhan.knockknock.service.user;

import com.shinhan.knockknock.domain.dto.user.CreateUserRequest;
import com.shinhan.knockknock.domain.dto.user.CreateUserResponse;
import com.shinhan.knockknock.domain.dto.user.ReadUserResponse;
import com.shinhan.knockknock.domain.dto.user.UpdateUserRequest;
import com.shinhan.knockknock.domain.entity.MatchEntity;
import com.shinhan.knockknock.domain.entity.TokenEntity;
import com.shinhan.knockknock.domain.entity.UserEntity;
import com.shinhan.knockknock.domain.entity.UserRoleEnum;
import com.shinhan.knockknock.repository.MatchRepository;
import com.shinhan.knockknock.repository.TokenRepository;
import com.shinhan.knockknock.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final MatchRepository matchRepository;
    private final TokenRepository tokenRepository;

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
    public Long readByUserId(String userId) {
        UserEntity findUser = userRepository.findByUserId(userId).orElse(null);
        assert findUser != null;
        return findUser.getUserNo();
    }

    @Override
    public Boolean readUserPhone(String phone) {
        UserEntity findUser = userRepository.findByUserPhone(phone);
        return findUser == null;
    }

    @Override
    public SingleMessageSentResponse sendSms(String phone, String validationNum) {
        SingleMessageSentResponse messageSentResponse = sendMessage(phone, validationNum);
        log.info("✉ Send Sms - ValidationNumber: {}", validationNum);
        return messageSentResponse;
    }

    @Override
    public CreateUserResponse createUser(CreateUserRequest request) {
        String randomPassword = null;
        boolean result = false;
        UserEntity entity = dtoToEntity(request);
        entity.setUserPassword(passwordEncoder.encode(request.getUserPassword()));
        if(request.getUserSimplePassword() != null) {
            entity.setUserSimplePassword(passwordEncoder.encode(request.getUserSimplePassword()));
        }
        if(request.getIsBioLogin()) {
            // 랜덤 비밀번호 생성
            randomPassword = generateRandomPassword(request.getUserPhone().substring(7));
            entity.setUserBioPassword(passwordEncoder.encode(randomPassword));
        }
        if(userRepository.existsByUserId(entity.getUserId())) {
            throw new DuplicateKeyException("이미 존재하는 아이디입니다.");
        }
        try {
            UserEntity createUser = userRepository.save(entity);
            if(createUser.getUserNo() != null)
                result = true;
        } catch(DataIntegrityViolationException exception) {
            if (exception.getCause() instanceof ConstraintViolationException) {
                // UNIQUE 제약조건 위반 시 처리할 코드
                throw new DuplicateKeyException("이미 가입된 회원입니다.");
            } else {
                // 기타 데이터 무결성 위반 처리
                throw new DataIntegrityViolationException("잘못된 요청입니다.");
            }
        }
        return CreateUserResponse.builder()
                .userBioPassword(randomPassword)
                .result(result)
                .build();
    }

    private String generateRandomPassword(String userPhone) {
        return UUID.randomUUID().toString().replace("-", "") + userPhone;
    }

    @Override
    public ReadUserResponse readUser(long userNo) {
        UserEntity user = userRepository.findById(userNo)
                .orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다."));

        ReadUserResponse readUserResponse = null;
        if(user.getUserType().equals(UserRoleEnum.PROTECTOR)){  // 보호자인 경우
            if(user.getMatchProtector() != null && user.getMatchProtector().getMatchStatus().equals("ACCEPT")) { // 매칭 정보가 있는 경우
                UserEntity protege = user.getMatchProtector().getUserProtege();
                readUserResponse = entityToDtoProtector(user, protege);
            } else {    // 매칭 정보가 없는 경우 본인 정보만 조회
                readUserResponse = entityToDtoProtector(user, user);
            }
        } else if(user.getUserType().equals(UserRoleEnum.PROTEGE)){ // 피보호자인 경우
            readUserResponse = entityToDtoProtector(user, user);
        } else {
            throw new RuntimeException("잘못된 요청입니다.");
        }

        return readUserResponse;
    }

    @Override
    public ReadUserResponse updateUser(long userNo, UpdateUserRequest request) {
        UserEntity user = userRepository.findById(userNo)
                .orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다."));

        ReadUserResponse response = null;
        if(user.getUserType().equals(UserRoleEnum.PROTECTOR)){
            if(user.getMatchProtector() != null && user.getMatchProtector().getMatchStatus().equals("ACCEPT")) {
                UserEntity protege = user.getMatchProtector().getUserProtege();
                UserEntity setProtege = setUpdateInfo(protege, request);
                userRepository.save(setProtege);
                response = entityToDtoProtector(user, setProtege);
            } else {
                throw new RuntimeException("매칭 정보가 없습니다.");
            }
        } else if(user.getUserType().equals(UserRoleEnum.PROTEGE)){
            UserEntity setProtege = setUpdateInfo(user, request);
            userRepository.save(setProtege);
            response = entityToDtoProtector(setProtege, setProtege);
        } else {
            throw new RuntimeException("잘못된 요청입니다.");
        }
        return response;
    }

    @Override
    public Boolean deleteUser(long userNo) {
        UserEntity user = userRepository.findById(userNo)
                .orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다."));

        // 매칭 삭제
        MatchEntity match;
        if(user.getUserType().equals(UserRoleEnum.PROTECTOR)){
            match = user.getMatchProtector();
        } else if(user.getUserType().equals(UserRoleEnum.PROTEGE)){
            match = user.getMatchProtege();
        } else {
            throw new RuntimeException("잘못된 요청입니다.");
        }
        if(match != null){
            matchRepository.delete(match);
        }

        user.setUserIsWithdraw(true);
        UserEntity deleteUser = userRepository.save(user);

        TokenEntity token = tokenRepository.findByUser(deleteUser)
                .orElseThrow(() -> new NoSuchElementException("토큰이 없습니다."));
        tokenRepository.delete(token);
        return deleteUser.isUserIsWithdraw();
    }

    private SingleMessageSentResponse sendMessage(String phone, String validationNum) {
        Message message = new Message();
        message.setFrom(fromPhoneNumber);
        message.setTo(phone);
        message.setType(MessageType.SMS);
        message.setText("[똑똑] 인증번호는 [" + validationNum + "] 입니다.");

        return this.defualtMessageService.sendOne(new SingleMessageSendingRequest(message));
    }

    private UserEntity setUpdateInfo(UserEntity user, UpdateUserRequest request) {
        String address = request.getUserAddress()+"/"+request.getUserAddressDetail();
        user.setUserBirth(request.getUserBirth());
        user.setUserGender(request.getUserGender());
        user.setUserAddress(address);
        user.setUserHeight(request.getUserHeight());
        user.setUserWeight(request.getUserWeight());
        user.setUserDisease(request.getUserDisease());

        return user;
    }
}
