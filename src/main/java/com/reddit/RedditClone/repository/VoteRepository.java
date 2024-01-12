package com.reddit.RedditClone.repository;

import com.reddit.RedditClone.models.Post;
import com.reddit.RedditClone.models.User;
import com.reddit.RedditClone.models.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
