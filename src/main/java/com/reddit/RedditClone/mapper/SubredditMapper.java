package com.reddit.RedditClone.mapper;

import com.reddit.RedditClone.dto.SubredditDto;
import com.reddit.RedditClone.models.Post;
import com.reddit.RedditClone.models.Subreddit;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubredditMapper {

    @Mapping(target = "numberOfPost", expression = "java(mapPost(subreddit.getPosts()))")
    SubredditDto mapSubredditToDto(Subreddit subreddit);

    default Integer mapPost(List<Post> numberOfPosts){
        return numberOfPosts.size();
    };

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    Subreddit mapDtoToSubreddit(SubredditDto subredditDto);
}
