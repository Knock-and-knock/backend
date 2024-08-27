package com.shinhan.knockknock.service.card;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ClovaOCRService {
    // API URL 설정
    @Value("${ClovaOCR_URL}")
    private String apiURL;
    @Value("${ClovaOCR_SECRET_KEY}")
    private String secretKey;

    public void ocrService() {
        try {
            // 연결 설정
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setReadTimeout(30000);
            con.setRequestMethod("POST");

            // 헤더 설정
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("X-OCR-SECRET", secretKey);

            // JSON 페이로드 생성
            JSONObject json = new JSONObject();
            json.put("version", "V1");
            json.put("requestId", UUID.randomUUID().toString());
            json.put("timestamp", System.currentTimeMillis());
            json.put("resultType", "string");

            // 이미지 정보 설정
            JSONArray images = new JSONArray();
            JSONObject image = new JSONObject();
            image.put("format", "png");
            image.put("name", "medium");
            image.put("url", "https://ocr.selvasai.com/img/sample_idcard.png");
            images.add(image);

            json.put("images", images);
            String postParams = json.toString();

            // POST 요청 전송
            con.connect();
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postParams.getBytes("UTF-8"));
                wr.flush();
            }

            // 응답 코드 확인
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) { // 성공
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 오류
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            // 응답 읽기
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            // JSON 응답 파싱
            JSONParser parser = new JSONParser();
            JSONObject responseObject = (JSONObject) parser.parse(response.toString());
            JSONArray imagesArray = (JSONArray) responseObject.get("images");
            JSONObject firstImage = (JSONObject) imagesArray.get(0);
            JSONArray fieldsArray = (JSONArray) firstImage.get("fields");

            String name = null;
            String idNumber = null;

            // 이름 처리
            for (Object fieldObj : fieldsArray) {
                JSONObject field = (JSONObject) fieldObj;
                String inferText = (String) field.get("inferText");

                if (inferText != null) {
                    if (inferText.matches(".*\\(\\)")) { // 이름 패턴
                        name = inferText.replaceAll("[()]", ""); // 괄호 제거
                    } else if (inferText.matches("\\d{6}-\\d{7}")) { // 주민등록번호 패턴
                        idNumber = inferText;
                    }
                }
            }

            // 추출된 값 출력
            System.out.println("이름: " + name);
            System.out.println("주민등록번호: " + idNumber);

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
