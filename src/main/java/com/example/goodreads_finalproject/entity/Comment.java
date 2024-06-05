package com.example.goodreads_finalproject.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "review_id")
    Review review;

    @Column(columnDefinition = "TEXT")
    String content;

    LocalDateTime deletedDateTime;
}
