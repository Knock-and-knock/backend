package com.shinhan.knockknock.exception;

public class TextToSpeechException extends RuntimeException {

    public TextToSpeechException(String message) {
        super(message);
    }

    public TextToSpeechException(String message, Throwable cause) {
        super(message, cause);
    }
}
