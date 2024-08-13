package com.shinhan.knockknock.service;

import net.nurigo.sdk.message.response.SingleMessageSentResponse;

public interface UserService {
    Boolean readUserId(String userId);
    Boolean readUserPhone(String phone);
    SingleMessageSentResponse sendSms(String phone, String validationNum);
}
