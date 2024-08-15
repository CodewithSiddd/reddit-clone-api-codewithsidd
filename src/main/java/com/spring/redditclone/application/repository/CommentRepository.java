package com.spring.redditclone.application.repository;

import com.spring.redditclone.application.model.Comment;
import com.spring.redditclone.application.model.Post;
import com.spring.redditclone.application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface CommentRepository   extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllCommentsByUser(User user);
}
