package com.bilgeadam.rabbitmq.consumer;

import com.bilgeadam.rabbitmq.model.MailRegisterModel;
import com.bilgeadam.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
@RequiredArgsConstructor
public class MailRegisterConsumer {
    private final MailService mailService;

    @RabbitListener(queues =  "mail-register-queue")

    public void addEmployee(MailRegisterModel mailRegisterModel) throws MessagingException {
        System.out.println(mailRegisterModel);
        mailService.sendMail(mailRegisterModel);
    }
}
