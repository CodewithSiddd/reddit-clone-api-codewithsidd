package com.spring.redditclone.application.model;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEmail {

    private String subject;

    private String recipent;

    private String body;

}
