package com.reddit.RedditClone.controller;

import com.reddit.RedditClone.dto.VoteDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/api/votes/")
public class VoteController {

    private final VoteService voteService;
    public ResponseEntity<void> vote(@RequestBody VoteDto voteDto){

    }
}
