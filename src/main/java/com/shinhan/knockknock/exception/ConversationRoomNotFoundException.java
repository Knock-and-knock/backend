package com.shinhan.knockknock.exception;

public class ConversationRoomNotFoundException extends RuntimeException {
    public ConversationRoomNotFoundException(String message) {
        super(message);
    }
}