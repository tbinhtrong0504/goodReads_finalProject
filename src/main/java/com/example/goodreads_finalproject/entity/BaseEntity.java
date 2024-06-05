package com.example.goodreads_finalproject.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @CreatedDate
//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime createdDateTime;

    @CreatedBy
    String createdBy;

    @LastModifiedDate
    LocalDateTime lastModifiedDateTime;

    @LastModifiedBy
    String lastModifiedBy;

}
