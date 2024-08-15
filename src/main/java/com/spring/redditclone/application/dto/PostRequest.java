package com.spring.redditclone.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {

    private Long postId;

    private String subredditName;

    private String postName;

    private String url;

    private String description;

}
