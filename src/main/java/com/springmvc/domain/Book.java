package com.springmvc.domain;


import com.springmvc.validator.BookId;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
public class Book {

    @BookId
    @Pattern(regexp = "ISBN[1-9]+", message = "{Pattern.NewBook.bookId}")
    private String bookId;         // 도서 ID

    @Size(min = 4, max = 50, message = "{Size.NewBook.name}" )
    private String name;           // 도서명

    @Min(value = 0, message = "{Min.NewBook.unitPrice}")
    @NotNull(message = "{NotNull.NewBook.unitPrice}")
    @Digits(integer = 8, fraction = 2, message = "{Digits.NewBook.unitPrice}")
    private int unitPrice;         // 가격
    private String author;         // 저자
    private String description;    // 설명
    private String publisher;      // 출판사
    private String category;       // 분류
    private int unitsInStock;      // 재고 수
    private String releaseDate;    // 출판일(월/년)
    private String condition;      // 신규 도서 또는 중고 도서 또는 전자책
    private MultipartFile bookImage; // 도서 이미지

    public Book(String bookId, String name, int unitPrice) {
        this.bookId = bookId;
        this.name = name;
        this.unitPrice = unitPrice;
    }

}
