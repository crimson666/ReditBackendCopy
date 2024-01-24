package com.reddit.RedditClone.service;

import com.reddit.RedditClone.dto.SubredditDto.SubredditDto;
import com.reddit.RedditClone.models.Subreddit;
import com.reddit.RedditClone.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

    private final SubredditRepository subredditRepository;

    @Transactional
    public Subreddit save (SubredditDto subredditDto){
        Subreddit subreddit = mapSubredditDto(subredditDto);
        Subreddit save = subredditRepository.save(subreddit);
        subredditDto.setId(save.getId());
        return subreddit;
    }

    private Subreddit mapSubredditDto(SubredditDto subredditDto) {
        return Subreddit.builder().name(subredditDto.getName()).description(subredditDto.getDescription()).build();
    }

    @Transactional(readOnly = true)
    public List<SubredditDto> getAllSubreddit() {
        return subredditRepository.findAll().stream().map(this::mapToDto).collect(toList());
    }

    private SubredditDto mapToDto(Subreddit subreddit){
        return SubredditDto.builder().name(subreddit.getName()).id(subreddit.getId()).numberOfPost(subreddit.getPosts().size()).build();
    }
}
