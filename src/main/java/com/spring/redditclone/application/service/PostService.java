package com.spring.redditclone.application.service;

import com.spring.redditclone.application.Exception.PostNotFoundException;
import com.spring.redditclone.application.Exception.SubredditNotFoundException;
import com.spring.redditclone.application.dto.PostRequest;
import com.spring.redditclone.application.dto.PostResponse;
import com.spring.redditclone.application.mapper.PostMapper;
import com.spring.redditclone.application.model.Post;
import com.spring.redditclone.application.model.Subreddit;
import com.spring.redditclone.application.model.User;
import com.spring.redditclone.application.repository.PostRepository;
import com.spring.redditclone.application.repository.SubredditRepository;
import com.spring.redditclone.application.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final SubredditRepository subredditRepository;
    private final AuthService authService;
    private final PostMapper postMapper;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Post save(PostRequest postRequest) throws SubredditNotFoundException{
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));
        User user = authService.getCurrentUser();
        return postRepository.save(postMapper.map(postRequest, subreddit, user));
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        List<PostResponse> responses = postRepository.findAll().stream().map(postMapper::mapToDto).collect(Collectors.toList());
        return responses;
    }

    @Transactional(readOnly = true)
    public PostResponse getPostById(Long postId) throws PostNotFoundException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUserName(String userName) {
        User user = userRepository.findByUserName(userName).orElseThrow(() -> new UsernameNotFoundException(userName));
        return postRepository.findByUser(user).stream().map(postMapper::mapToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long subredditId) throws SubredditNotFoundException {
        Subreddit subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new SubredditNotFoundException(subredditId.toString()));
        List<Post> posts = postRepository.findAllBySubreddit(subreddit);
        return posts.stream().map(postMapper::mapToDto).collect(Collectors.toList());
    }

}
