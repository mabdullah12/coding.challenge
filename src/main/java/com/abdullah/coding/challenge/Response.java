package com.abdullah.coding.challenge;

import org.springframework.http.HttpStatus;

public class Response<T> {
    private final HttpStatus statusCode;
    private final String message;
    private final T data;

    public Response(HttpStatus statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
