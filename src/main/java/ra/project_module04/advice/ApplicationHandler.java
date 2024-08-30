package ra.project_module04.advice;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ra.project_module04.constants.EHttpStatus;
import ra.project_module04.exception.CustomException;
import ra.project_module04.model.dto.resp.ResponseWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ApplicationHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
        return ResponseEntity.badRequest().body(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.FAILED)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .data(errors)
                        .build()
        );
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException ex) {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.FAILED)
                        .statusCode(ex.getHttpStatus().value())
                        .data(ex.getMessage())
                        .build(),
                ex.getHttpStatus()
        );
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ResponseWrapper> handleNoSuchElementException(NoSuchElementException ex) {
        // Giả sử bạn đã định nghĩa EHttpStatus để phản ánh trạng thái HTTP
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.FAILED) // Cần phải có định nghĩa cho EHttpStatus
                        .statusCode(HttpStatus.NOT_FOUND.value()) // Hoặc ex.getHttpStatus().value() nếu có
                        .data(ex.getMessage())
                        .build(),
                HttpStatus.NOT_FOUND // Hoặc ex.getHttpStatus() nếu có
        );
    }

//    @ExceptionHandler(AuthenticationException.class)
//    public ResponseEntity<?> handleAuthenException(AuthenticationException e) {
//        Map<String,Object> map = new HashMap<>();
//        map.put("error",new CustomException(400,"BAD_REQUEST",e));
//        return ResponseEntity.badRequest().body(map);
//    }
//
//    @ExceptionHandler(AccessDeniedException.class)
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    public Map<String,Object> forbidden(AccessDeniedException e){
//        Map<String,Object> map = new HashMap<>();
//        map.put("error",new CustomException(403,"FOR_BIDDEN",e.getMessage()));
//        return  map;
//    }
}
