package com.reddit.RedditClone.repository;

import com.reddit.RedditClone.models.Comment;
import com.reddit.RedditClone.models.Post;
import com.reddit.RedditClone.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}
