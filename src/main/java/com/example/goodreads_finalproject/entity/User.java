package com.example.goodreads_finalproject.entity;

import com.example.goodreads_finalproject.statics.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseEntity {

    @Column(unique = true, nullable = false)
    String email;

    @Column(nullable = false)
    String password;

    @Column(nullable = false, columnDefinition = "BOOLEAN default false")
    boolean activated;

    @Column(nullable = false, columnDefinition = "BOOLEAN default false")
    boolean locked;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Column(nullable = false)
    String avatar;

    @Column(nullable = false)
    String fullName;

    @Enumerated(EnumType.STRING)
    Gender gender;

    LocalDate dob;

    @Column(nullable = false)
    String phone;

    @OneToOne
    @JoinColumn(name = "ward_code")
    Ward address;

    @Column(name = "street")
    String street;

    @Column(columnDefinition = "TEXT")
    String about;

    LocalDateTime deletedDateTime;
}
