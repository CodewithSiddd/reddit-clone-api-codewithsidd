package com.spring.redditclone.application.mapper;

import com.spring.redditclone.application.dto.SubredditDto;
import com.spring.redditclone.application.model.Post;
import com.spring.redditclone.application.model.Subreddit;
import com.spring.redditclone.application.model.User;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubredditMapper {

    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
    SubredditDto mapSubredditEntityToDto(Subreddit subreddit);

    default Integer mapPosts(List<Post> numberOfPosts) { return numberOfPosts.size(); }

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "user.userId", source = "user.userId")
    Subreddit mapSubredditDtoToEntity(SubredditDto subredditDto, User user);
}
