package com.spring.redditclone.application.service;

import com.spring.redditclone.application.Exception.RedditCloneException;
import com.spring.redditclone.application.dto.SubredditDto;
import com.spring.redditclone.application.mapper.SubredditMapper;
import com.spring.redditclone.application.model.Subreddit;
import com.spring.redditclone.application.model.User;
import com.spring.redditclone.application.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;
    private final AuthService authService;

    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
        User user = authService.getCurrentUser();
        Subreddit saveSubreddit = subredditRepository.save(subredditMapper.mapSubredditDtoToEntity(subredditDto, user));
        subredditDto.setId(saveSubreddit.getId());
        return subredditDto;
    }

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll().stream()
                .map(subredditMapper::mapSubredditEntityToDto)
                .collect(Collectors.toList());
    }

    public SubredditDto getSubredditById(Long id) throws RedditCloneException {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new RedditCloneException("No subreddit found with given id"));
        return subredditMapper.mapSubredditEntityToDto(subreddit);
    }
}
