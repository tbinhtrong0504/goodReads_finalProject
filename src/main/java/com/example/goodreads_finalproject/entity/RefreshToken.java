package com.example.goodreads_finalproject.entity;


import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "refresh_token")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefreshToken extends BaseEntity {

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id")
    User user;

    @Column(name = "refresh_token")
    String refreshToken;

    //    @Type(type= "org.hibernate.type.NumericBooleanType")
    @Column(columnDefinition = "boolean default false")
    boolean invalidated;

}
