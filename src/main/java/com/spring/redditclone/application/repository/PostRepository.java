package com.spring.redditclone.application.repository;

import com.spring.redditclone.application.model.Post;
import com.spring.redditclone.application.model.Subreddit;
import com.spring.redditclone.application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);
}
