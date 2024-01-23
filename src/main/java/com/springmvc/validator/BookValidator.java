package com.springmvc.validator;
import com.springmvc.domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import javax.validation.ConstraintViolation;
import java.util.HashSet;
import java.util.Set;
@Component
public class BookValidator implements Validator {
    @Autowired
    private javax.validation.Validator beanValidator;  // bean validation(JSR-303 validator)의 인스턴스 선언
    private Set<Validator> springValidators;  //spring validation(Validator 인터페이스)의 인스턴스 선언
    public BookValidator() {   //BookValidator 생성자
        springValidators = new HashSet<Validator>(); // BookValidator 객체 생성시, springValidators에 Validator인터페이스 형의 HashSet 객체 생성
    }
    public void setSpringValidators(Set<Validator> springValidators) {  // springValidators의 setter() 메서드
        this.springValidators = springValidators;
    }
    public boolean supports(Class<?> clazz) {  // Book 클래스의 유효성 검사를 위한 여부 메서드
        return Book.class.isAssignableFrom(clazz);
    }
    public void validate(Object target, Errors errors) {  // Book 클래스의 유효성 검사 메서드
        //target은 주어진 객체로, 이에 대해 유효성검사를 수행. 오류발생시 errors 객체에 저장
        Set<ConstraintViolation<Object>> violations = beanValidator.validate(target);  // Bean Validation 설정
        // bean validation 오류 저장
        for (ConstraintViolation<Object> violation : violations) {
            String propertyPath = violation.getPropertyPath().toString();  // 오류 발생 필드 저장
            String message = violation.getMessage();  // 오류 발생 메시지 저장
            errors.rejectValue(propertyPath, "", message);  // 오류 발생된 필드와 메시지를 Errors 객체에 저장
        }
        // spring validation 오류 저장
        for(Validator validator: springValidators) {
            validator.validate(target, errors);  // 발생된 오류 정보를 전달
        }
    }
}