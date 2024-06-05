package com.example.goodreads_finalproject.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    String typeOfNotification;

    @Column(columnDefinition = "TEXT")
    String content;

    @Column(nullable = false, columnDefinition = "BOOLEAN default false")
    boolean seen;

    @CreatedDate
    LocalDateTime createdDateTime;

}
