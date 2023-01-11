package com.chess.common;

public class InvalidMoveException extends RuntimeException {
    public InvalidMoveException(String errorMessage) {
        super(errorMessage);
    }
}