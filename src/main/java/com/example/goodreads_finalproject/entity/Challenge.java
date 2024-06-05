package com.example.goodreads_finalproject.entity;

import com.example.goodreads_finalproject.statics.ChallengeStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "challenges")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Challenge extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    Integer numberOfBook;

    @Enumerated(EnumType.STRING)
    ChallengeStatus challengeStatus;

    LocalDate startedDate;

    LocalDate endDate;

}
