package com.mrs.exception;

public class DuplicateMovieException extends Exception {
    public DuplicateMovieException(String message) {
        super(message);
    }
}