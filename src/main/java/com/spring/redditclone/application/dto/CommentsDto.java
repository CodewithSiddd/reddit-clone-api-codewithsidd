package com.spring.redditclone.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentsDto {

    private Long id;

    private Long postId;

    private Instant createdDate;

    private String userName;

    @NotBlank
    private String text;

}
