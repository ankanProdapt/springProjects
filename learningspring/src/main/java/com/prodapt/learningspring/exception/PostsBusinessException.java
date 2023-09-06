package com.prodapt.learningspring.exception;


public class PostsBusinessException extends RuntimeException{
    public PostsBusinessException(String message) {
        super(message);
    }
}
