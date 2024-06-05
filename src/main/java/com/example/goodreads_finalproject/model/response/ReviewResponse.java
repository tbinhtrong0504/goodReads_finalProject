package com.example.goodreads_finalproject.model.response;

import com.example.goodreads_finalproject.entity.Category;
import com.example.goodreads_finalproject.entity.Comment;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewResponse {
    Long currentUserId;

    Long reviewId;

    Long userReviewId;

    String avatar;

    String name;

    LocalDate reviewDate;

    double ratingDetail;

    String content;

    Integer likes;

    Boolean following;

    Boolean liked;

    List<CommentResponse> childComments;

    Integer comments;

}
