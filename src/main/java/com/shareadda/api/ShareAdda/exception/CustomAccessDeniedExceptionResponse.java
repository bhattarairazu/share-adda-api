package com.shareadda.api.ShareAdda.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomAccessDeniedExceptionResponse {
    private String message;

    public CustomAccessDeniedExceptionResponse() {
        this.message = "Unauthorized access.You do not have permission to access this resource.";
    }
}
