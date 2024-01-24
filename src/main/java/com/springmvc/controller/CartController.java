package com.springmvc.controller;

import com.springmvc.domain.Book;
import com.springmvc.domain.Cart;
import com.springmvc.domain.CartItem;
import com.springmvc.exception.BookIdException;
import com.springmvc.service.BookService;
import com.springmvc.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private BookService bookService;

    // request에서 session 값을 얻어 Id 추출해서 sessionId에 저장
    // 그 sessionId로 cart에 리다이렉트
    @GetMapping
    public String requestCartId(HttpServletRequest request) {
        String sessionId = request.getSession(true).getId();
        return "redirect:/cart/" + sessionId;
    }

    // RequestBody를 이용해서 Cart 객체 생성
    @PostMapping
    public @ResponseBody Cart create(@RequestBody Cart cart) {
        return cartService.create(cart);
    }

    //cartId 값으로 매핑
    //PathVariable로 cartId를 파라미터로 받음
    //cart 변수에 cartId 값 저장
    //model을 이용해서 값 cart에 저장후 반환
    @GetMapping("/{cartId}")
    public String requestCartList(@PathVariable(value="cartId") String cartId, Model model) {
        Cart cart = cartService.read(cartId);
        model.addAttribute("cart", cart);
        return "cart";
    }

    // RequestBody를 이용해서 cartId 객체 생성
    @PutMapping("/{cartId}")
    public @ResponseBody Cart read(@PathVariable(value = "cartId") String cartId) {
        return cartService.read(cartId);
    }

    @PutMapping("/add/{bookId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT) // 오류시 나오는 응답 코드 설정
    public void addCartByNewItem(@PathVariable String bookId, HttpServletRequest request) {
        // 장바구니 ID인 세션ID 가져오기
        String sessionId = request.getSession(true).getId();
        Cart cart = cartService.read(sessionId); // 장바구니에 등록된 모든 정보 얻어오기
        if (cart == null) {
            cart = cartService.create(new Cart(sessionId));
        }
        // 경로 변수 bookId에 대한 정보 얻어오기
        Book book = bookService.getBookById(bookId);

        if (book == null) {
            throw new IllegalArgumentException(new BookIdException(bookId));
        }
        // bookId에 대한 도서 정보를 장바구니에 등록하기
        cart.addCartItem(new CartItem(book));
        cartService.update(sessionId, cart); // 세션 ID에 대한 장바구니 갱신
    }

    @PutMapping("/remove/{bookId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void removeCartByItem(@PathVariable String bookId, HttpServletRequest request) {
        // 장바구니 ID인 세션 ID 가져오기
        String sessionId = request.getSession(true).getId();
        Cart cart = cartService.read(sessionId); // 장바구니에 등록된 모든 정보 얻어오기
        if (cart == null) {
         cart = cartService.create(new Cart(sessionId));
        }
        //경로 변수 bookId에 대한 정보 얻어오기
        Book book = bookService.getBookById(bookId);
        if (book == null) {
            throw new IllegalArgumentException(new BookIdException(bookId));
        }
            // bookId에 대한 도서 정보를 장바구니에서 삭제하기
            cart.removeCartItem(new CartItem(book));
            cartService.update(sessionId, cart); // 세션 ID에 대한 장바구니 갱신하기
    }
}
