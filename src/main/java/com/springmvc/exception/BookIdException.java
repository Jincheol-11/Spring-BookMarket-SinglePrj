package com.springmvc.exception;

import lombok.Getter;

@Getter
@SuppressWarnings("serial")
public class BookIdException extends RuntimeException{

    private String bookId;

    public BookIdException(String bookId) {
        this.bookId = bookId;
    }
}
