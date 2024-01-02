package com.springmvc.repository;

import com.springmvc.domain.Cart;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class CartRepositoryImpl implements CartRepository {

    private Map<String, Cart> listOfCarts;

    public CartRepositoryImpl() {
        listOfCarts = new HashMap<String, Cart>();
    }

    // Cart의 cartId를 봐서 이미 포함되어 있다면 예외를 던짐
    // 포함되어 있지 않다면 장바구니에 저장
    @Override
    public Cart create(Cart cart) {
        if (listOfCarts.keySet().contains(cart.getCartId())) {
            throw new IllegalArgumentException(String.format("장바구니를 생성할 수 없습니다. 장바구니 id(%)가 존재합니다"
            , cart.getCartId()));
        }
        listOfCarts.put(cart.getCartId(), cart);
        return cart;
    }

    // cartId를 받은 listOfCarts 읽음
    @Override
    public Cart read(String cartId) {
        return listOfCarts.get(cartId);
    }

    // cartId와 cart를 받아와서 갱신
    // cartId가 포함되어 있는지 체크
    @Override
    public void update(String cartId, Cart cart) {
        if (!listOfCarts.keySet().contains(cartId)) {
            //장바구니 ID가 존재하지 않은 경우 예외 처리
            throw new IllegalArgumentException(String.format("장바구니 " +
                    "목록을 갱신할 수 없습니다. 장바구니 id(%)가 존재하지 않습니다", cartId));
        }
        listOfCarts.put(cartId, cart);
    }

    @Override
    public void delete(String cartId) {

    }
}
