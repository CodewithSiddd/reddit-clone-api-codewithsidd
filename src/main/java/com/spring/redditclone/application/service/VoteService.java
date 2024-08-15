package com.spring.redditclone.application.service;

import com.spring.redditclone.application.Exception.PostNotFoundException;
import com.spring.redditclone.application.Exception.RedditCloneException;
import com.spring.redditclone.application.dto.VoteDto;
import com.spring.redditclone.application.model.Post;
import com.spring.redditclone.application.model.Vote;
import com.spring.redditclone.application.repository.PostRepository;
import com.spring.redditclone.application.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.spring.redditclone.application.model.VoteType.UPVOTE;

@Service
@AllArgsConstructor
public class VoteService {

    private final PostRepository postRepository;
    private final VoteRepository voteRepository;
    private final AuthService authService;

    public void vote(VoteDto voteDto) throws PostNotFoundException {
        Post post = postRepository.findById(voteDto.getPostId()).orElseThrow(() -> new PostNotFoundException("Post not found for id - " + voteDto.getPostId()));
        Optional<Vote> vote = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
        if(vote.isPresent() && vote.get().getVoteType().equals(voteDto.getVoteType())) {
            throw new RedditCloneException("You have already " + voteDto.getVoteType() + "'d for this post.");
        }
        if(UPVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }
        voteRepository.save(mapToDto(voteDto, post));
        postRepository.save(post);
    }

    private Vote mapToDto(VoteDto voteDto, Post post) {
        return Vote.builder().voteType(voteDto.getVoteType()).post(post).user(authService.getCurrentUser()).build();
    }

}
