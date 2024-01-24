package com.reddit.RedditClone.dto.SubredditDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubredditDto {
    private long id;
    private String name;
    private String description;
    private Integer numberOfPost;

}
