package com.reddit.RedditClone.controller;

import com.reddit.RedditClone.dto.SubredditDto.SubredditDto;
import com.reddit.RedditClone.service.SubredditService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("api/subreddit")
@AllArgsConstructor
@Slf4j
public class SubredditController {

    private final SubredditService subredditService;

    @PostMapping
    public void createSubreddit(@RequestBody SubredditDto subredditDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(subredditService.save(subredditDto));
    }

    @GetMapping
    Public ResponseEntity<List<SubredditBto>> getAllSubreddit(){
        return ResponseEntity.status(HttpStatus.OK).body(subredditService.getAllSubreddit());
    }
}
