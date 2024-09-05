package com.shinhan.knockknock.service.user;

import com.shinhan.knockknock.domain.dto.user.*;
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

    /**
     * <pre>
     * 메소드명   : readUserId
     * 설명       : 주어진 userId로 사용자가 존재하는지 확인한다.
     *
     * @param userId   존재 여부를 확인할 사용자 ID
     * @return         사용자가 존재하지 않으면 true, 존재하면 false
     * @throws RuntimeException 영문과 숫자가 아닌 경우 발생
     * </pre>
     */
    @Override
    public Boolean readUserId(String userId) {
        if(!validateUserId(userId))
            throw new RuntimeException("영문, 숫자만 입력 가능합니다.");
        UserEntity findUser = userRepository.findByUserId(userId).orElse(null);
        return findUser == null;
    }

    private boolean validateUserId(String userId) {
        String USER_ID_PATTERN = "^[a-zA-Z0-9]+$";
        return userId.matches(USER_ID_PATTERN);
    }

    /**
     * <pre>
     * 메소드명   : readByUserId
     * 설명       : 주어진 userId로 사용자를 조회하고 해당 사용자의 번호를 반환한다.
     *
     * @param userId   조회할 사용자 ID
     * @return         사용자의 고유 번호
     * @throws AssertionError 사용자가 존재하지 않는 경우 발생
     * </pre>
     */
    @Override
    public Long readByUserId(String userId) {
        UserEntity findUser = userRepository.findByUserId(userId).orElse(null);
        assert findUser != null;
        return findUser.getUserNo();
    }

    /**
     * <pre>
     * 메소드명   : readUserPhone
     * 설명       : 주어진 전화번호로 사용자가 존재하는지 확인한다.
     *
     * @param phone   존재 여부를 확인할 사용자 전화번호
     * @return        사용자가 존재하지 않으면 true, 존재하면 false
     * </pre>
     */
    @Override
    public Boolean readUserPhone(String phone) {
        UserEntity findUser = userRepository.findByUserPhone(phone);
        return findUser == null;
    }

    /**
     * <pre>
     * 메소드명   : sendSms
     * 설명       : 주어진 전화번호로 SMS를 발송하고 발송 결과를 반환한다.
     *
     * @param phone           SMS를 보낼 전화번호
     * @param validationNum   인증번호
     * @return                발송 결과 객체
     * </pre>
     */
    @Override
    public SingleMessageSentResponse sendSms(String phone, String validationNum) {
        SingleMessageSentResponse messageSentResponse = sendMessage(phone, validationNum);
        log.info("📩 Send Sms - ValidationNumber: {}", validationNum);
        return messageSentResponse;
    }

    /**
     * <pre>
     * 메소드명   : createUser
     * 설명       : 사용자를 생성하고, 생체인증 비밀번호를 설정할 경우 랜덤 비밀번호를 생성하여 설정한다.
     *
     * @param request   생성할 사용자 정보
     * @return          생성된 사용자 정보와 결과
     * @throws DuplicateKeyException    이미 존재하는 아이디 또는 회원일 경우 발생
     * @throws DataIntegrityViolationException 데이터 무결성 위반 시 발생
     * </pre>
     */
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

    /**
     * <pre>
     * 메소드명   : readUser
     * 설명       : 사용자 번호를 통해 사용자의 정보를 조회하여 반환한다.
     *
     * @param userNo   조회할 사용자 번호
     * @return         조회된 사용자 정보
     * @throws NoSuchElementException 사용자나 매칭 정보가 없는 경우 발생
     * </pre>
     */
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

    /**
     * <pre>
     * 메소드명   : updateUser
     * 설명       : 사용자 번호를 통해 사용자의 정보를 업데이트하고, 매칭된 사용자 정보도 함께 업데이트한다.
     *
     * @param userNo   업데이트할 사용자 번호
     * @param request  업데이트할 사용자 정보
     * @return         업데이트된 사용자 정보
     * @throws NoSuchElementException 사용자나 매칭 정보가 없는 경우 발생
     * @throws RuntimeException 매칭 정보가 없거나 잘못된 요청인 경우 발생
     * </pre>
     */
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

    /**
     * <pre>
     * 메소드명   : deleteUser
     * 설명       : 사용자 번호를 통해 사용자를 삭제하고, 관련 매칭과 토큰 정보를 삭제한다.
     *
     * @param userNo   삭제할 사용자 번호
     * @return         사용자가 성공적으로 삭제되었는지 여부
     * @throws NoSuchElementException 사용자나 매칭 또는 토큰 정보가 없는 경우 발생
     * </pre>
     */
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

    /**
     * <pre>
     * 메소드명   : readSimplePayment
     * 설명       : 간편결제 비밀번호가 등록되어 있는지 확인한다.
     *
     * @param userNo   확인할 사용자 번호
     * @return         간편결제 비밀번호 상태와 결과 메시지
     * @throws NoSuchElementException 사용자가 존재하지 않는 경우 발생
     * </pre>
     */
    @Override
    public SimplePaymentResponse readSimplePayment(long userNo) {
        UserEntity user = userRepository.findById(userNo)
                .orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다."));
        String message = "";
        boolean result = false;
        if(user.getUserPaymentPassword() == null){
            message = "간편결제 비밀번호를 등록해주세요.";
        } else {
            message = "간편결제 비밀번호가 등록되어 있습니다.";
            result = true;
        }

        return SimplePaymentResponse.builder()
                .message(message)
                .result(result)
                .build();
    }

    /**
     * <pre>
     * 메소드명   : createSimplePayment
     * 설명       : 사용자의 간편결제 비밀번호를 설정한다.
     *
     * @param userNo   설정할 사용자 번호
     * @param request  설정할 비밀번호 정보
     * @return         설정 결과와 메시지
     * @throws NoSuchElementException 사용자가 존재하지 않는 경우 발생
     * @throws RuntimeException 비밀번호 형식이 맞지 않거나 설정 중 오류가 발생한 경우 발생
     * </pre>
     */
    @Override
    public SimplePaymentResponse createSimplePayment(long userNo, SimplePaymentRequest request) {
        UserEntity user = userRepository.findById(userNo)
                .orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다."));

        String paymentPassword = request.getUserPaymentPassword();
        log.info("🔑 createSimplePayment - userNo : {}, paymentPassword : {}", user.getUserNo(), request.getUserPaymentPassword());
        if(!paymentPassword.isEmpty() && paymentPassword.matches("^[0-9]{6}$")) // 6자리 숫자인지 확인
            user.setUserPaymentPassword(passwordEncoder.encode(request.getUserPaymentPassword())); // 간편결제 비밀번호 인코딩
        else
            throw new RuntimeException("비밀번호는 6자리 숫자만 설정할 수 있습니다.");

        try {
            userRepository.save(user);
        } catch(Exception e) {
            throw new RuntimeException("간편결제 비밀번호 등록 중 오류가 발생하였습니다..");
        }

        return SimplePaymentResponse.builder()
                .message("간편 결제 비밀번호를 등록하였습니다.")
                .result(true)
                .build();
    }

    /**
     * <pre>
     * 메소드명   : validateSimplePayment
     * 설명       : 사용자의 간편결제 비밀번호를 검증한다.
     *
     * @param userNo   검증할 사용자 번호
     * @param request  검증할 비밀번호 정보
     * @return         검증 성공 여부와 메시지
     * @throws NoSuchElementException 사용자가 존재하지 않는 경우 발생
     * @throws RuntimeException 비밀번호가 일치하지 않는 경우 발생
     * </pre>
     */
    @Override
    public SimplePaymentResponse validateSimplePayment(long userNo, SimplePaymentRequest request) {
        UserEntity user = userRepository.findById(userNo)
                .orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다."));
        log.info("🔑 validateSimplePayment - userNo : {}, paymentPassword : {}", user.getUserNo(), request.getUserPaymentPassword());
        if(!passwordEncoder.matches(request.getUserPaymentPassword(), user.getUserPaymentPassword()))
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");

        return SimplePaymentResponse.builder()
                .message("비밀번호 인증에 성공하였습니다.")
                .result(true)
                .build();
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
        String address = request.getUserAddress()!=null
                ? request.getUserAddress()+"/"+request.getUserAddressDetail()
                : null;
        user.setUserBirth(request.getUserBirth());
        user.setUserGender(request.getUserGender());
        user.setUserAddress(address);
        user.setUserHeight(request.getUserHeight());
        user.setUserWeight(request.getUserWeight());
        user.setUserDisease(request.getUserDisease());

        return user;
    }
}
