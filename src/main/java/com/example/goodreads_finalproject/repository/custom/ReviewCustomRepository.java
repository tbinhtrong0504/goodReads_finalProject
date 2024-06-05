package com.example.goodreads_finalproject.repository.custom;

import com.example.goodreads_finalproject.entity.Comment;
import com.example.goodreads_finalproject.model.request.ReviewRequest;
import com.example.goodreads_finalproject.model.response.AvgRatingResponse;
import com.example.goodreads_finalproject.model.response.CommentResponse;
import com.example.goodreads_finalproject.model.response.ReviewResponse;
import com.example.goodreads_finalproject.repository.BaseRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ReviewCustomRepository extends BaseRepository {

    public void removeRating(Long bookId, Long userId) {
        String sql = "UPDATE reviews rv SET rv.rating=0 WHERE rv.book_id = :bookId AND rv.user_id = :userId";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("bookId", bookId);
        parameters.put("userId", userId);
        getNamedParameterJdbcTemplate().update(sql, parameters);
    }

    public void changeRating(ReviewRequest request, Long userId) {
        String sql = "UPDATE reviews rv SET rv.rating= :rating WHERE rv.book_id = :bookId AND rv.user_id = :userId";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("rating", request.getRating());
        parameters.put("bookId", request.getBookId());
        parameters.put("userId", userId);
        getNamedParameterJdbcTemplate().update(sql, parameters);
    }

    public void removeReview(Long bookId, Long userId) {
        String sql = "DELETE FROM reviews rv WHERE rv.book_id = :bookId AND rv.user_id = :userId";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("bookId", bookId);
        parameters.put("userId", userId);
        getNamedParameterJdbcTemplate().update(sql, parameters);
    }

    public List<AvgRatingResponse> calculateAvgRating(Long bookId) {
        String sql = "SELECT rv.rating, count(rv.rating) AS countOfRating FROM reviews rv WHERE rv.book_id = :bookId AND rv.rating>0 GROUP BY rv.rating";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("bookId", bookId);
        return getNamedParameterJdbcTemplate().query(sql, parameters, BeanPropertyRowMapper.newInstance(AvgRatingResponse.class));
    }

    public List<ReviewResponse> getAllReviews(Long bookId, Long userId) {
        Map<String, Object> parameters = new HashMap<>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT ");
        sql.append("u.id userReviewId, ");
        sql.append("u.avatar avatar, ");
        sql.append("u.full_name name, ");
        sql.append("rv.id reviewId, ");
        sql.append("rv.content, ");
        sql.append("rv.rating ratingDetail, ");
        sql.append("Date(rv.created_date_time) reviewDate, ");
        sql.append("COUNT(likes.id) likes ");
        sql.append("FROM users u ");
        sql.append("LEFT JOIN reviews rv ON u.id = rv.user_id ");
        sql.append("LEFT JOIN likes ON rv.id = likes.review_id ");
        sql.append("WHERE rv.book_id = :bookId ");
        sql.append("GROUP BY  u.id,  u.avatar,  u.full_name, rv.id,  rv.content,  rv.rating, rv.created_date_time ");
        parameters.put("bookId", bookId);

        List<ReviewResponse> reviewResponses = getNamedParameterJdbcTemplate().query(sql.toString(), parameters, BeanPropertyRowMapper.newInstance(ReviewResponse.class));

        for (ReviewResponse rv : reviewResponses) {
            rv.setCurrentUserId(userId);

            List<CommentResponse> childComments = getAllCommentsOfReview(rv.getReviewId());
            rv.setChildComments(childComments);
            rv.setComments(childComments == null ? 0 : childComments.size());

            Boolean following = checkRelationship(userId, rv.getUserReviewId());
            rv.setFollowing(following);

            Boolean liked = checkLiked(userId, rv.getReviewId());
            rv.setLiked(liked);
        }
//        reviewResponses.removeIf(rv -> rv.getUserReviewId().equals(userId));
        return reviewResponses;
    }

    public List<CommentResponse> getAllCommentsOfReview(Long reviewId) {
        Map<String, Object> parameters = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("cm.id, ");
        sql.append("u.id userCommentId, ");
        sql.append("u.avatar, ");
        sql.append("u.full_name name, ");
        sql.append("cm.content contentOfComment, ");
        sql.append("DATE(cm.created_date_time) commentDate ");
        sql.append("FROM comments cm ");
        sql.append("LEFT JOIN users u ON cm.user_id = u.id ");
        sql.append("WHERE cm.review_id = :reviewId ");

        parameters.put("reviewId", reviewId);
        return getNamedParameterJdbcTemplate().query(sql.toString(), parameters, BeanPropertyRowMapper.newInstance(CommentResponse.class));
    }

    public Boolean checkRelationship(Long currentUserId, Long anonymousUserId) {
        try {
            Map<String, Object> parameters = new HashMap<>();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ");
            sql.append("CASE ");
            sql.append(" WHEN following_status = 'FOLLOWING' THEN TRUE  ELSE FALSE ");
            sql.append(" END AS following ");
            sql.append("FROM followings ");
            sql.append("WHERE user_request_id = :currentUserId and user_accept_id= :anonymousUserId");
            parameters.put("currentUserId", currentUserId);
            parameters.put("anonymousUserId", anonymousUserId);
            return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), parameters, Boolean.class);
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean checkLiked(Long currentUserId, Long reviewId) {
        try {
            Map<String, Object> parameters = new HashMap<>();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ");
            sql.append("CASE WHEN COUNT(likes.id) > 0 THEN true ELSE false END AS liked ");
            sql.append("FROM likes ");
            sql.append("WHERE likes.user_id = :currentUserId AND likes.review_id= :reviewId");
            parameters.put("currentUserId", currentUserId);
            parameters.put("reviewId", reviewId);
            return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), parameters, Boolean.class);
        } catch (Exception e) {
            return false;
        }
    }

}
