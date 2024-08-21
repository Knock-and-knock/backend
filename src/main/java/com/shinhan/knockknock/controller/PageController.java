package com.shinhan.knockknock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class PageController {

    @GetMapping("/conversation")
    public RedirectView conversationPage() {
        return new RedirectView("/conversation.html");
    }

    @GetMapping("/stt")
    public RedirectView sttPage() {
        return new RedirectView("/stt.html");
    }
}
