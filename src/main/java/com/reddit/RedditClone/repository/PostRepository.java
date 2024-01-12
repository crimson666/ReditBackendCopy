package com.reddit.RedditClone.repository;

import com.reddit.RedditClone.models.Post;
import com.reddit.RedditClone.models.Subreddit;
import com.reddit.RedditClone.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);
}
