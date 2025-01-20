package com.vita.back.api.model.data;

import lombok.Data;

@Data
public class UserIdDupCheckDto {
    private String userId;
    private String userCi;
}
