package com.friendzoo.exception.advice;


import com.friendzoo.exception.CustomJWTException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * CustomControllerAdvice
 */
@Slf4j
@RestControllerAdvice
public class CustomControllerAdvice {


    /**
     * NoSuchElementException 발생시 처리
     *
     * @param e NoSuchElementException
     * @return ResponseEntity , Optional.isPresent(), Optional.get() 등에서 없을 때 발생!
     */
    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<?> notExist(NoSuchElementException e) {

        String msg = e.getMessage();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("msg", msg)); // "msg": "No value present"
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<?> notExist(EntityNotFoundException e) {

        String msg = e.getMessage();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("msg", msg));
    }

    // 따라서, NoSuchElementException은 컬렉션이나 옵셔널에서 주로 사용되며,
    // EntityNotFoundException은 JPA와 관련된 엔티티 검색에서 사용되는 예외입니다. 두 예외는 서로 포함 관계가 없으며, 각각 별도의 상황에서 사용됩니다.

//  @ExceptionHandler(MethodArgumentNotValidException.class)
//  protected ResponseEntity<?> handleIllegalArgumentException(MethodArgumentNotValidException e) {
//
//      String msg = e.getMessage();
//
//      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(Map.of("msg", msg));
//  }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {

        String msg = e.getMessage();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", msg));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> handleIllegalArgumentException(MethodArgumentNotValidException e) {

        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        log.error("handleIllegalArgumentException errors: {}", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }


    /**
     * 컨트롤러 메소드의 매개변수 타입과 클라이언트가 전달한
     * 요청 값의 타입이 일치하지 않을 때 발생
     *
     * @param ex MethodArgumentTypeMismatchException
     * @return ResponseEntity
     */

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String>
    handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException
                                             ex) {
        String errorMessage = String.format(
                "Invalid argument: '%s'. Expected type: '%s'.",
                ex.getValue(),
                ex.getRequiredType().getSimpleName()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    /**
     * 요청 바디가 JSON 형식이 아닐 때 발생
     *
     * @param ex HttpMessageNotReadableException
     * @return ResponseEntity
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String>
    handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Invalid request body: " + ex.getMessage());
    }

    @ExceptionHandler(CustomJWTException.class)
    protected ResponseEntity<?> handleJWTException(CustomJWTException e) {

        String msg = e.getMessage();

        return ResponseEntity.ok().body(Map.of("error", msg));
    }

}
