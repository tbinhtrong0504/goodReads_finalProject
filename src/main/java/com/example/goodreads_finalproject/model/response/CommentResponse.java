package com.example.goodreads_finalproject.model.response;

import com.example.goodreads_finalproject.entity.Comment;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse {

    Long id;

    Long userCommentId;

    String avatar;

    String name;

    String contentOfComment;

    LocalDate commentDate;

}
