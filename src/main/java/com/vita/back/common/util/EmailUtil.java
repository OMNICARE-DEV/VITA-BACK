package com.vita.back.common.util;

import com.vita.back.api.model.data.SendEmailDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.internet.MimeMessage;
import java.util.Map;

@Component
public class EmailUtil {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${email.fromEmail}")
    private String fromEmail;

    @Value("${email.fromName}")
    private String fromName;

    public EmailUtil(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public boolean sendMail(SendEmailDto request) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setFrom(fromEmail, fromName);

            // 이메일 발송 대상, 제목 설정
            helper.setTo(request.getToEmail());
            helper.setSubject(request.getSubject());

            String content = "";
            if("HTML".equals(request.getType())) {
                Context context = new Context();
                context.setVariables(request.getParams());
                content = templateEngine.process(request.getTemplatePath(), context);
            } else {
                content = request.getContent();
            }

            // 이메일 본문 설정
            helper.setText(content, true);

            // 이메일 발송
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}