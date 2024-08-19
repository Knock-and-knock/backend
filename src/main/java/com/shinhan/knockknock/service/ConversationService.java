package com.shinhan.knockknock.service;

import com.shinhan.knockknock.domain.dto.conversationroom.ChatbotResponse;
import com.shinhan.knockknock.domain.dto.conversationroom.ConversationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ConversationService {

    @Autowired
    ChatbotService chatbotService;

    @Autowired
    TextToSpeechService textToSpeechService;

    public byte[] conversation(ConversationRequest request) {
        ChatbotResponse response = chatbotService.chatbot(request.getInput());
        System.out.println(response);

        String voice = "alloy";
        String outputFileName = "output";
        return textToSpeechService.convertTextToSpeech(response.getContent(), voice, outputFileName);
    }

}
