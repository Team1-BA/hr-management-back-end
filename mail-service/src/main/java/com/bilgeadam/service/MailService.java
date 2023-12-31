package com.bilgeadam.service;


import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.MailException;
import com.bilgeadam.rabbitmq.model.AddEmployeeMailModel;
import com.bilgeadam.rabbitmq.model.GuestMailRegisterModel;
import com.bilgeadam.rabbitmq.model.MailForgotPassModel;
import com.bilgeadam.rabbitmq.model.MailRegisterModel;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailService {


    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public String sendMail(MailRegisterModel mailRegisterModel) throws MessagingException {

        String CONFIRMATION_URL = "http://localhost:9090/api/v1/auth/user-active?token=" + mailRegisterModel.getActivationLink();
        System.out.println("Confirmation URL: " + CONFIRMATION_URL);
        String templateName = "authentication-email";
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED,
                StandardCharsets.UTF_8.name()
        );
        Map<String, Object> properties = new HashMap<>();
        if (mailRegisterModel.getPersonalEmail() != null) {
            properties.put("companyEmail", mailRegisterModel.getPersonalEmail());
        } else if (mailRegisterModel.getCompanyEmail() != null) {
            properties.put("companyEmail", mailRegisterModel.getCompanyEmail());
        }
        if (mailRegisterModel.getUsername() != null) {
            properties.put("username", mailRegisterModel.getUsername());
        } else if (mailRegisterModel.getName() != null) {
            properties.put("username", mailRegisterModel.getName());
        }

        CONFIRMATION_URL = String.format(CONFIRMATION_URL);
        properties.put("confirmationUrl", CONFIRMATION_URL);
        Context context = new Context();
        context.setVariables(properties);
        helper.setFrom("bouali.social@gmail.com");
        helper.setTo(mailRegisterModel.getCompanyEmail());
        helper.setSubject("Welcome to our nice platform");
        String template = templateEngine.process(templateName, context);
        helper.setText(template, true);
        mailSender.send(mimeMessage);
        return "Successful";
    }


    public String sendMail(AddEmployeeMailModel mailModel) throws MessagingException {


        String CONFIRMATION_URL = "http://localhost:9090/api/v1/auth/user-active?token=" + mailModel.getActivationLink();
        System.out.println("Confirmation URL: " + CONFIRMATION_URL);


        String templateName = "authentication-email";
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED,
                StandardCharsets.UTF_8.name()
        );
        Map<String, Object> properties = new HashMap<>();
        properties.put("username", mailModel.getUsername());
        properties.put("companyEmail", mailModel.getCompanyEmail());
        CONFIRMATION_URL = String.format(CONFIRMATION_URL);
        properties.put("confirmationUrl", CONFIRMATION_URL);
        Context context = new Context();
        context.setVariables(properties);
        helper.setFrom("bouali.social@gmail.com");
        helper.setTo(mailModel.getPersonalEmail());
        helper.setSubject("Welcome to our Human Resources platform");
        String template = templateEngine.process(templateName, context);
        helper.setText(template, true);
        mailSender.send(mimeMessage);
        return "Successful";
    }


    public void sendForgotPassword(MailForgotPassModel mailForgotPassModel) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("your_email@example.com");
            if (mailForgotPassModel.getCompanyEmail() != null) {
                mailMessage.setTo(mailForgotPassModel.getCompanyEmail());
            } else mailMessage.setTo(mailForgotPassModel.getPersonalEmail());
            mailMessage.setSubject("Dear " + mailForgotPassModel.getUsername());
            mailMessage.setText("Your new password is below. \n\n Password: " + mailForgotPassModel.getRandomPassword());
            mailSender.send(mailMessage);
        } catch (MailException e) {
            throw new MailException(ErrorType.MAIL_SEND_ERROR);
        }
    }


    public String sendGuestActivationMail(GuestMailRegisterModel guestMailRegisterModel) throws MessagingException {
        String CONFIRMATION_URL = "http://localhost:9090/api/v1/auth/user-active?token=" + guestMailRegisterModel.getActivationLink();
        String templateName = "authentication-email";
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED,
                StandardCharsets.UTF_8.name()
        );
        Map<String, Object> properties = new HashMap<>();
        properties.put("username", guestMailRegisterModel.getUsername());
        CONFIRMATION_URL = String.format(CONFIRMATION_URL);
        properties.put("confirmationUrl", CONFIRMATION_URL);
        Context context = new Context();
        context.setVariables(properties);
        helper.setTo(guestMailRegisterModel.getPersonalEmail());
        helper.setSubject("Welcome to our nice platform");
        String template = templateEngine.process(templateName, context);
        helper.setText(template, true);
        mailSender.send(mimeMessage);
        return "Successful";
    }


}
