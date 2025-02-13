package com.serevin.patyforboost.dto;

import com.serevin.patyforboost.exception.ApiException;
import lombok.Getter;

import java.util.Map;

@Getter
public class ApiErrorResponse {
    private final String message;
    private final String code;
    private final Map<String, Object> data;

    public ApiErrorResponse(ApiException e) {
        this.message = e.getMessage();
        this.code = e.getCode();
        this.data = e.getData();
    }

}