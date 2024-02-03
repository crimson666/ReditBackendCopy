package com.reddit.RedditClone.controller;

import com.reddit.RedditClone.dto.SubredditDto;
import com.reddit.RedditClone.service.SubredditService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("api/subreddit")
@AllArgsConstructor
@Slf4j
public class SubredditController {

    private final SubredditService subredditService;

    @PostMapping
    public ResponseEntity<SubredditDto> createSubredit(@RequestBody SubredditDto subreditDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subredditService.save(subreditDto));
    }
    @GetMapping
    public ResponseEntity<List<SubredditDto>> getAllSubredit() {
        return ResponseEntity.status(HttpStatus.OK).body(subredditService.getAllSubreddit());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubredditDto> getSubredit(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(subredditService.getSubreddit(id));
    }
}
