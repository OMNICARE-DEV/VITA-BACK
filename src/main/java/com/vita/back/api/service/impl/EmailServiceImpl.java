package com.vita.back.api.service.impl;

import com.vita.back.api.model.data.SendEmailDto;
import com.vita.back.api.service.EmailService;
import com.vita.back.common.exception.VitaException;
import com.vita.back.common.util.EmailUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {
    private final EmailUtil emailUtil;

    public EmailServiceImpl(EmailUtil emailUtil) {
        this.emailUtil = emailUtil;
    }

    /**
     * 이메일 발송 샘플
     */
    public boolean sendEmail(SendEmailDto request) throws VitaException {
        request.setSubject("이메일 발송 테스트입니다용가리");
        request.setContent("내용도 걍 대충 넣습니다람쥐");
        //HTML인 경우 샘플 html 템플릿 넣어줌, TEXT인 경우는 content에 내용 넣어주면 됨
        if("HTML".equals(request.getType())) {
            request.setTemplatePath("email-template");

            Map<String, Object> params = new HashMap<>();;
            params.put("name", request.getToName());
            params.put("content", request.getContent());
            params.put("url", "https://www.naver.com");

            request.setParams(params);
        }

        return emailUtil.sendMail(request);
    }
}
