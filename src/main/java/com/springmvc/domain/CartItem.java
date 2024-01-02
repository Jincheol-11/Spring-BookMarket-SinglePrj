package com.springmvc.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CartItem {
    private Book book; // 도서
    private int quantity; // 도서 개수
    private int totalPrice; // 도서 가격

    public CartItem(Book book) {
        super();
        this.book = book;
        this.quantity = 1;
        this.totalPrice = book.getUnitPrice();
    }

    public void updateTotalPrice() {
        totalPrice = this.book.getUnitPrice() * this.quantity;
    }

    // book이 null이라면 0, null이 아니라면 책의 해쉬코드 값을 더함
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( (book == null) ? 0 : book.hashCode());
        return result;
    }

    // 선택한 책이 obj라면 참값 반환, 아니라면 false
    // 두 객체의 클래스가 동일 하지 않다면 false
    // 값을 저장한 other.book의 값이 null이 아니면 false, this book과 other book이 같지 않다면 false
    // 즉 this book, other book 모두 null 값일 때 true 반환
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CartItem other = (CartItem) obj;
        if (book == null) {
            if (other.book != null)
                return false;
        } else if (!book.equals(other.book))
            return false;
        return true;
    }
}
