package com.joukou.fxexchangeservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {

    private String message;
    private String errorCode;
    private int status;
    private LocalDateTime timestamp;
    private String path;
}
