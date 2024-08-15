package com.spring.redditclone.application.service;

import com.spring.redditclone.application.Exception.RedditCloneException;
import com.spring.redditclone.application.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;

    @Async
    void sendMail(NotificationEmail notificationEmail) throws RedditCloneException {
        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom("CodeWithSidd@gemail.com");
            mimeMessageHelper.setSubject(notificationEmail.getSubject());
            mimeMessageHelper.setTo(notificationEmail.getRecipent());
            mimeMessageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));
    };
    try{
        mailSender.send(mimeMessagePreparator);
        log.info("Mail with Activation Link is sent.");
    } catch (MailException e) {
        log.error("Got a error while sending email ", e.getMessage());
        throw new RedditCloneException("Exception while sending Email.");
    }

    }
}
