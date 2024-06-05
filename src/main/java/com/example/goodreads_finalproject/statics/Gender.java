package com.example.goodreads_finalproject.statics;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
    MALE("Male"), FEMALE("Female"), OTHER("Other"), UNDEFINED("Undefined");

    String name;

    public static Gender convertGender(String value) {
        for (Gender gender : Gender.values()) {
            if (gender.getName().equalsIgnoreCase(value)) {
                return gender;
            }
        }
        return Gender.UNDEFINED;
    }
}
