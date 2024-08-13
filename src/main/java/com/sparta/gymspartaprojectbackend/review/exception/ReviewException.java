package com.sparta.gymspartaprojectbackend.review.exception;

import com.sparta.gymspartaprojectbackend.enums.ErrorType;
import com.sparta.gymspartaprojectbackend.exception.CustomException;

public class ReviewException extends CustomException {
    public ReviewException(ErrorType errorType) {
        super(errorType);
    }
}