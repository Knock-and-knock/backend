package com.shinhan.knockknock.service.conversation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinhan.knockknock.exception.TextToSpeechException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class TextToSpeechService {

    @Value("${OPENAI_API_KEY}")
    private String apiKey;

    @Value("${TTS_VOICE}")
    private String voice;

    private static final String API_URL = "https://api.openai.com/v1/audio/speech";

    /**
     * <pre>
     * 메소드명   : convertTextToSpeech
     * 설명       : 텍스트를 음성으로 변환하는 API를 호출하여 음성 데이터를 반환한다.
     *
     * @param input   음성으로 변환할 텍스트 입력
     * @return        변환된 음성 데이터 (MP3 파일 형식의 byte 배열)
     * @throws TextToSpeechException  JSON 변환 실패 또는 API 호출 실패 시 발생
     * </pre>
     */
    public byte[] convertTextToSpeech(String input) {
        try {
            // ObjectMapper를 사용하여 JSON 본문 생성
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> requestBodyMap = new HashMap<>();
            requestBodyMap.put("model", "tts-1");
            requestBodyMap.put("input", input);
            requestBodyMap.put("voice", voice);
            String requestBody = objectMapper.writeValueAsString(requestBodyMap);

            // RestTemplate 객체 생성
            RestTemplate restTemplate = new RestTemplate();

            // HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

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
        } catch (JsonProcessingException e) {
            throw new TextToSpeechException("Failed to create JSON request body.", e);
        }
    }
}
