package com.spring.redditclone.application.service;

import com.spring.redditclone.application.Exception.PostNotFoundException;
import com.spring.redditclone.application.Exception.RedditCloneException;
import com.spring.redditclone.application.dto.CommentsDto;
import com.spring.redditclone.application.mapper.CommentMapper;
import com.spring.redditclone.application.model.Comment;
import com.spring.redditclone.application.model.NotificationEmail;
import com.spring.redditclone.application.model.Post;
import com.spring.redditclone.application.model.User;
import com.spring.redditclone.application.repository.CommentRepository;
import com.spring.redditclone.application.repository.PostRepository;
import com.spring.redditclone.application.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class CommentService {

    private static final String POSTURL = "";
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public void save(CommentsDto commentsDto) throws PostNotFoundException, RedditCloneException {
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));
        Comment mapper = commentMapper.mapToEntity(commentsDto, post, authService.getCurrentUser());
        commentRepository.save(mapper);

        String msg = mailContentBuilder.build(post.getUser().getUserName() + " has posted a comment on your post." + POSTURL);
        sendCommentEmailNoti(msg, post.getUser());
    }

    private void sendCommentEmailNoti(String msg, User user) throws RedditCloneException {
        mailService.sendMail(new NotificationEmail(user.getUserName() + " Commented on your post.", user.getEmail(), msg));
    }

    public List<CommentsDto> getAllCommentsForPost(Long postId) throws PostNotFoundException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post).stream().map(commentMapper::mapToDto).collect(Collectors.toList());
    }

    public List<CommentsDto> getAllCommentsForUser(String userName) {
        User user = userRepository.findByUserName(userName).orElseThrow(() -> new UsernameNotFoundException(userName));
        return commentRepository.findAllCommentsByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
