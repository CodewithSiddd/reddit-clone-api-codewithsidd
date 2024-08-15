package com.spring.redditclone.application.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.spring.redditclone.application.dto.PostRequest;
import com.spring.redditclone.application.dto.PostResponse;
import com.spring.redditclone.application.model.Post;
import com.spring.redditclone.application.model.Subreddit;
import com.spring.redditclone.application.model.User;
import com.spring.redditclone.application.repository.CommentRepository;
import com.spring.redditclone.application.repository.VoteRepository;
import com.spring.redditclone.application.service.AuthService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private AuthService authService;

    @Mapping(target = "CreatedDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "subreddit.id", source = "subreddit.id")
    @Mapping(target = "user.userId", source = "user.userId")
    @Mapping(target = "voteCount", constant = "0")
    public abstract Post map(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "id", source = "postId")
//    @Mapping(target = "postName", source = "postName")
//    @Mapping(target = "description", source = "description")
//    @Mapping(target = "url", source = "url")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "userName", source = "user.userName")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    public abstract PostResponse mapToDto(Post post);

    Integer commentCount(Post post) {
        return commentRepository.findByPost(post).size();
    }

    String getDuration(Post post) {
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }

}
