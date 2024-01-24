package com.springmvc.controller;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.springmvc.exception.BookIdException;
import com.springmvc.exception.CategoryException;
import com.springmvc.validator.BookValidator;
import com.springmvc.validator.UnitsInStockValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.springmvc.domain.Book;
import com.springmvc.service.BookService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Log4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/books") // 기본값이 겟맵핑
public class BookController {

    private final BookService bookService;

    private final BookValidator bookValidator; // BookValidator 인스턴스 선언

    // booklist로 jsp에 list 값을 전달
    @GetMapping
    public String requestBookList(Model model) {
        List<Book> list = bookService.getAllBookList();
        model.addAttribute("bookList", list);
        return "books";
    }

    // ModelAndView사용
    // Model로 사용하고 싶다면 String으로 바꾸고 변수에 Model넣으면 된다.
    @GetMapping("/all")
    public ModelAndView requestAllBooks() {
        ModelAndView modelAndView = new ModelAndView();
        List<Book> list = bookService.getAllBookList();
        modelAndView.addObject("bookList", list);
        modelAndView.setViewName("books");
        return modelAndView;
    }


    @GetMapping("/{category}")
    public String requestBooksByCategory(@PathVariable("category") String bookCategory, Model model) {
        List<Book> booksByCategory =bookService.getBookListByCategory(bookCategory);

        // 카테고리가 null값이거나 비어있다면 예외처리
        if (booksByCategory == null || booksByCategory.isEmpty()) {
            throw new CategoryException();
        }
        model.addAttribute("bookList", booksByCategory);
        return "books";
    }

    @GetMapping("/filter/{bookFilter}")
    public String requestBooksByFilter(
            @MatrixVariable(pathVar="bookFilter") Map<String,List<String>> bookFilter,
            Model model) {
        Set<Book> booksByFilter = bookService.getBookListByFilter(bookFilter);
        model.addAttribute("bookList", booksByFilter);
        return "books";
    }

    // id를 쿼리스트링으로 가져와서 넘김
    @GetMapping("/book")
    public String requestBookById(@RequestParam("id") String bookId, Model model) {
        Book bookById = bookService.getBookById(bookId);
        model.addAttribute("book", bookById );
        return "book";
    }

    @GetMapping("/add")
    public String requestAddBookForm(@ModelAttribute("NewBook") Book book) {
        return "addBook";
    }

    // valid로 유효성 검사 시행
    @PostMapping("/add")
    public String submitAddNewBook(@Valid @ModelAttribute("NewBook") Book book,
        BindingResult result) {

        if (result.hasErrors()) {
            return "addBook";
        }
        // 멀티파일파트
        MultipartFile bookImage = book.getBookImage();
        String saveName = bookImage.getOriginalFilename();
        // OriginalFilename을 saveName으로 지정하고 그 saveName을 이름으로 지정
        File saveFile = new File("C:\\upload", saveName);

        // 이미지가 있을 때
        if (bookImage != null && !bookImage.isEmpty()) {
            try {
                bookImage.transferTo(saveFile);
            } catch (Exception e) {
                throw new RuntimeException("도서 이미지 업로드에 실패했습니다.", e);
            }
        }
        // 새 책 업로드
        bookService.setNewBook(book);
        log.info("========================================="+book.getBookId());
        return "redirect:/books";
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("addTitle", "신규 도서 등록");
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(bookValidator); // 생성한 unitsInStockValidator 설정
        binder.setAllowedFields("bookId", "name", "unitPrice", "author", "description", "publisher", "category"
        , "unitsInStock", "totalPages", "releaseDate", "condition", "bookImage");
    }

    @ExceptionHandler(value = {BookIdException.class})
    public ModelAndView handleError (HttpServletRequest req, BookIdException exception) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("invalidBookId", exception.getBookId());
        mav.addObject("exception", exception);
        mav.addObject("url", req.getRequestURL() + "?" + req.getQueryString());
        mav.setViewName("errorBook");
        return mav;
    }

}
