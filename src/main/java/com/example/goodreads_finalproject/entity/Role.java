package com.example.goodreads_finalproject.entity;

import com.example.goodreads_finalproject.statics.Roles;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    Roles name;

}
