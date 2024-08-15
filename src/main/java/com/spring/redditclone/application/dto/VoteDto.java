package com.spring.redditclone.application.dto;

import com.spring.redditclone.application.model.VoteType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoteDto {

    private VoteType voteType;

    private Long postId;

}
