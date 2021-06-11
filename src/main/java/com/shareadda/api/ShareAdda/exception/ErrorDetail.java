package com.shareadda.api.ShareAdda.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDetail {
    private String title;
    private String message;
    private int status;
    private long timeStamp;
    private String developerMessage;
    @Builder.Default
    private Map<String, List<ValidationError>> errors = new HashMap<String,List<ValidationError>>();
}
