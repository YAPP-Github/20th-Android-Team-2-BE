package yapp.bestFriend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import yapp.bestFriend.model.dto.DefaultRes;

@ControllerAdvice
public class ExceptionController {

    // @Valid의 유효성 검증을 통과하지 못하면, MethodArgumentValidException이 발생한다
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DefaultRes<String>> processValidationError(MethodArgumentNotValidException exception){

        BindingResult bindingResult = exception.getBindingResult();

        StringBuilder builder = new StringBuilder();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("](은)는 ");
            builder.append(fieldError.getDefaultMessage());
            builder.append(" 입력된 값: [");
            builder.append(fieldError.getRejectedValue());
            builder.append("]");
        }

        return new ResponseEntity<>(DefaultRes.response(HttpStatus.BAD_REQUEST.value(),
                builder.toString()),null, HttpStatus.BAD_REQUEST);
    }
}
