package com.example.goodreads_finalproject.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book_Of_Challenge")

@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookOfChallenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    Book book;

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    Challenge challenge;
}
