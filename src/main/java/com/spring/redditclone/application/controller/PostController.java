package com.spring.redditclone.application.controller;

import com.spring.redditclone.application.Exception.PostNotFoundException;
import com.spring.redditclone.application.Exception.SubredditNotFoundException;
import com.spring.redditclone.application.dto.PostRequest;
import com.spring.redditclone.application.dto.PostResponse;
import com.spring.redditclone.application.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequest postRequest) throws SubredditNotFoundException {
        postService.save(postRequest);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getAllPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) throws PostNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostById(id));
    }

    @GetMapping("/by-subreddit/[id]")
    public ResponseEntity<List<PostResponse>> getPostsBySubreddit(@PathVariable Long id) throws SubredditNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostsBySubreddit(id));
    }

    @GetMapping("/by-user/{name}")
    public ResponseEntity<List<PostResponse>> getPostsByUserName(@PathVariable String userName) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostsByUserName(userName));
    }
}
