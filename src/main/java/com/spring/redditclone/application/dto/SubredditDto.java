package com.spring.redditclone.application.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubredditDto {

    private Long id;

    private String name;

    private String description;

    private Integer numberOfPosts;
}
