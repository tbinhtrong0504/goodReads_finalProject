package com.example.goodreads_finalproject.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "provinces")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Province {
    @Id
    String code;

    String name;

    String nameEn;

    String fullName;

    String fullNameEn;

    String codeName;

}
