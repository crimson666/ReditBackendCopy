package com.reddit.RedditClone.service;

import com.reddit.RedditClone.dto.SubredditDto;
import com.reddit.RedditClone.exception.SpringRedditException;
import com.reddit.RedditClone.mapper.SubredditMapper;
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
    private final SubredditMapper subredditMapper;

    @Transactional
    public SubredditDto save (SubredditDto subredditDto){
        Subreddit subreddit = subredditMapper.mapDtoToSubreddit(subredditDto);
        Subreddit save = subredditRepository.save(subreddit);
        subredditDto.setId(save.getId());
        return subredditDto;
    }

    @Transactional(readOnly = true)
    public List<SubredditDto> getAllSubreddit() {
        return subredditRepository.findAll().stream().map(subredditMapper::mapSubredditToDto).collect(toList());
    }

    public SubredditDto getSubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id).orElseThrow(()-> new SpringRedditException("No Subreddit found"));
        return subredditMapper.mapSubredditToDto(subreddit);
    }
}
