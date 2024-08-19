package com.shinhan.knockknock.service;

import com.shinhan.knockknock.exception.TextToSpeechException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class TextToSpeechService {

    @Value("${OPENAI_API_KEY}")
    private String apiKey;

    private static final String API_URL = "https://api.openai.com/v1/audio/speech";

    /**
     * <pre>
     * 메소드명 : convertTextToSpeech
     * 설명     : 텍스트를 음성으로 변환하여 MP3 파일로 저장한다.
     * </pre>
     * @param input 텍스트 입력
     * @param voice 음성 유형
     * @param outputFileName 저장할 파일 이름
     */
    public byte[] convertTextToSpeech(String input, String voice, String outputFileName) {
        // RestTemplate 객체 생성
        RestTemplate restTemplate = new RestTemplate();

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        // 요청 본문 데이터 생성
        String requestBody = String.format(
                "{\"model\": \"tts-1\", \"input\": \"%s\", \"voice\": \"%s\"}",
                input,
                voice
        );

        // 요청 생성
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // API 호출 및 응답 받기
        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(
                API_URL,
                HttpMethod.POST,
                requestEntity,
                byte[].class
        );

        // 응답 상태 코드 확인 및 처리
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            byte[] audioData = responseEntity.getBody();
            if (audioData != null) {
                return audioData;
            } else {
                throw new TextToSpeechException("Failed to receive audio data from API.");
            }
        } else {
            throw new TextToSpeechException("Failed to request TTS API: " + responseEntity.getStatusCode());
        }
    }
}
