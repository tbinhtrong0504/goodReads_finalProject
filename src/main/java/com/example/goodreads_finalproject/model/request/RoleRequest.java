package com.example.goodreads_finalproject.model.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleRequest extends BaseSearchRequest{

    @NotBlank
    String name;
}
