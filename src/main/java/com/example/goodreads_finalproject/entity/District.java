package com.example.goodreads_finalproject.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "districts")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class District {
    @Id
    String code;

    String name;

    String nameEn;

    String fullName;

    String fullNameEn;

    String codeName;

    @ManyToOne
    @JoinColumn(name = "province_code")
    Province province;
}
