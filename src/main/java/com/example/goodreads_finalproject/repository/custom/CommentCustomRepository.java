package com.example.goodreads_finalproject.repository.custom;

import com.example.goodreads_finalproject.entity.Comment;
import com.example.goodreads_finalproject.model.request.ReviewRequest;
import com.example.goodreads_finalproject.model.response.AvgRatingResponse;
import com.example.goodreads_finalproject.model.response.CommentResponse;
import com.example.goodreads_finalproject.model.response.ReviewResponse;
import com.example.goodreads_finalproject.repository.BaseRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class CommentCustomRepository extends BaseRepository {


    public Optional<Comment> findCommentByUserAndReview(Long reviewId, Long userId) {
        String sql = "SELECT * FROM `comments` cm WHERE cm.review_id = :reviewId AND cm.user_id = :userId";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("reviewId", reviewId);
        parameters.put("userId", userId);
        List<Comment> comments = getNamedParameterJdbcTemplate().query(sql, parameters, BeanPropertyRowMapper.newInstance(Comment.class));
        if (comments.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(comments.get(0));
        }
    }


}
