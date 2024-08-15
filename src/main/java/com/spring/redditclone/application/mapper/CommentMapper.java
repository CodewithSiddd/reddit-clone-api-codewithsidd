package com.spring.redditclone.application.mapper;

//import com.spring.redditclone.application.dto.CommentsDto;
import com.spring.redditclone.application.dto.CommentsDto;
import com.spring.redditclone.application.model.Comment;
import com.spring.redditclone.application.model.Post;
import com.spring.redditclone.application.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source="commentsDto.text")
    @Mapping(target = "createdDate", expression="java(java.time.Instant.now())")
    @Mapping(target = "post", source="post")
    @Mapping(target = "user", source = "user")
    Comment mapToEntity(CommentsDto commentsDto, Post post, User user);

    @Mapping(target = "postId", source = "comment.post.postId")
    @Mapping(target = "userName", source = "comment.user.userName")
    CommentsDto mapToDto(Comment comment);

}
