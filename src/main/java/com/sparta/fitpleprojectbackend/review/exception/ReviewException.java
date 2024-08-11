package com.sparta.fitpleprojectbackend.review.exception;

import com.sparta.fitpleprojectbackend.enums.ErrorType;
import com.sparta.fitpleprojectbackend.exception.CustomException;

public class ReviewException extends CustomException {
    public ReviewException(ErrorType errorType) {
        super(errorType);
    }
}