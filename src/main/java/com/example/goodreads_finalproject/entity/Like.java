package com.example.goodreads_finalproject.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.mapping.ToOne;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "likes")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Like extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "review_id")
    Review review;
}
