package com.sparta.fltpleprojectbackend.review.exception;

import com.sparta.fltpleprojectbackend.enums.ErrorType;
import com.sparta.fltpleprojectbackend.exception.CustomException;

public class ReviewException extends CustomException {
    public ReviewException(ErrorType errorType) {
        super(errorType, errorType.getMessage());
    }
}
