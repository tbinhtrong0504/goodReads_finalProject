package com.example.goodreads_finalproject.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sharings")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Sharing extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    String title;

    String content;

    LocalDateTime deletedDateTime;

}
