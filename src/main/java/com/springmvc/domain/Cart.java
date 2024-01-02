package com.springmvc.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Cart {
    private String cartId; // 장바구니 ID
    private Map<String, CartItem> cartItems; // 장바구니 항목
    private int grandTotal; // 총액

    public Cart() {
        cartItems = new HashMap<String, CartItem>();
        grandTotal = 0;
    }

    public Cart(String cartId) {
        this();
        this.cartId = cartId;
    }

    // grandTotal 값 초기화
    // cartItem을 한바퀴 돌려서 총액을 구함
    public void updateGrandTotal() {
        grandTotal = 0;
        for (CartItem item : cartItems.values()) {
            grandTotal = grandTotal + item.getTotalPrice();
        }
    }

    // cartitem 클래스와 논리 구조가 동일
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( (cartId == null) ? 0 : cartId.hashCode());
        return result;
    }

    // cartitem 클래스와 논리 구조가 동일
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Cart other = (Cart) obj;
        if (cartId == null) {
            if (other.cartId != null)
                return false;
        } else if (!cartId.equals(other.cartId))
            return false;
        return true;
    }

    public void addCartItem(CartItem item) {
        String bookId = item.getBook().getBookId(); // 현재 등록하기 위한 도서 ID 가져오기

        //도서 ID가 cartItems 객체에 등록되어 있는지 여부 확인
        if (cartItems.containsKey(bookId)) {
            CartItem cartItem = cartItems.get(bookId); // 등록된 도서 ID 정보 가져오기
            // 등록된 도서 ID 개수 추가 저장 (기존 수량에 새로운 수량을 더해서 저장)
            cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
            cartItems.put(bookId, cartItem); // 등록된 도서 ID에 대한 변경 정보(cartItem) 저장
        } else {
            cartItems.put(bookId, item); // 도서 ID에 대한 도서 정보(item) 저장
        }
        updateGrandTotal(); // 총액 갱신
    }

    public void removeCartItem(CartItem item) {
        String bookId = item.getBook().getBookId();
        cartItems.remove(bookId); // bookId 도서 삭제
        updateGrandTotal(); // 총액 갱신
    }

}
